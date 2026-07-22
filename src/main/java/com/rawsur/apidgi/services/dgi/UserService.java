package com.rawsur.apidgi.services.dgi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rawsur.apidgi.dto.dgi.user.UserEntrepriseAssociationDto;
import com.rawsur.apidgi.dto.dgi.user.UserIntermDto;
import com.rawsur.apidgi.dto.dgi.user.UserResponse;
import com.rawsur.apidgi.exceptions.NotFoundException;
import com.rawsur.apidgi.models.dgi.Entreprise;
import com.rawsur.apidgi.models.dgi.Intermediary;
import com.rawsur.apidgi.models.dgi.User;
import com.rawsur.apidgi.models.dgi.UserEntreprise;
import com.rawsur.apidgi.models.dgi.UserInterm;
import com.rawsur.apidgi.models.keycloak.UserKeycloak;
import com.rawsur.apidgi.repositories.dgi.EntrepriseRepo;
import com.rawsur.apidgi.repositories.dgi.IntermediaryRepo;
import com.rawsur.apidgi.repositories.dgi.UserEntrepriseRepo;
import com.rawsur.apidgi.repositories.dgi.UserIntermRepo;
import com.rawsur.apidgi.repositories.dgi.UserRepo;
import com.rawsur.apidgi.routes.Routes;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private Environment env;

    @Autowired
    private UserIntermRepo userIntermRepo;

    @Autowired
    private IntermediaryRepo intermediaryRepo;

    @Autowired
    private EntrepriseRepo entrepriseRepo;

    @Autowired
    private UserEntrepriseRepo userEntrepriseRepo;

    @Autowired
    private KeycloakTools kcTools;

    @Transactional
    public List<UserInterm> createUserInterms(UserIntermDto usersIntermDto) {
        if (usersIntermDto == null || usersIntermDto.getUser() == null
                || usersIntermDto.getUser().getKeycloakId() == null
                || usersIntermDto.getUser().getKeycloakId().isBlank()) {
            throw new NotFoundException("Utilisateur invalide pour l'affectation");
        }

        User foundUser = this.userRepo.findByUsername(usersIntermDto.getUser().getKeycloakId());

        if (foundUser == null) {
            User user = new User();
            user.setUsername(usersIntermDto.getUser().getKeycloakId());
            user.setKeycloakId(usersIntermDto.getUser().getKeycloakId());
            user.setFname(usersIntermDto.getUser().getFname());
            user.setLname(usersIntermDto.getUser().getLname());
            foundUser = this.userRepo.save(user);
        }

        if (usersIntermDto.getEntreprises() != null) {
            for (UserEntrepriseAssociationDto entrepriseAssociation : usersIntermDto.getEntreprises()) {
                if (entrepriseAssociation != null && entrepriseAssociation.getIdEntreprise() != null) {
                    this.associateUserToEntreprise(
                            foundUser,
                            entrepriseAssociation.getIdEntreprise(),
                            entrepriseAssociation.getIsActive());
                }
            }
        }

        List<UserInterm> uInterms = new ArrayList<>();
        List<Intermediary> interms = usersIntermDto.getInterms() != null
                ? usersIntermDto.getInterms()
                : Collections.emptyList();

        for (Intermediary intermediary : interms) {
            if (intermediary == null || intermediary.getId() == null) {
                continue;
            }
            Intermediary persisted = this.intermediaryRepo.findById(intermediary.getId()).orElse(null);
            if (persisted == null) {
                continue;
            }
            UserInterm userInterm = new UserInterm();
            userInterm.setUser(foundUser);
            userInterm.setInterm(persisted);
            uInterms.add(userInterm);
        }

        this.userIntermRepo.deleteByUser(foundUser.getId());
        if (uInterms.isEmpty()) {
            return Collections.emptyList();
        }
        return this.userIntermRepo.saveAll(uInterms);
    }

    @Transactional
    public UserEntreprise associateUserToEntreprise(User user, UUID idEntreprise, Boolean isActive) {
        Entreprise entreprise = this.entrepriseRepo.findById(idEntreprise)
                .orElseThrow(() -> new NotFoundException("Entreprise non trouvee avec l'ID: " + idEntreprise));

        UserEntreprise userEntreprise = this.userEntrepriseRepo.findByUserAndEntreprise(user, entreprise)
                .orElseGet(UserEntreprise::new);

        userEntreprise.setUser(user);
        userEntreprise.setEntreprise(entreprise);
        userEntreprise.setIsActive(isActive != null ? isActive : true);

        return this.userEntrepriseRepo.save(userEntreprise);
    }

    public List<UserKeycloak> getUsersKeycloak() {
        final String realm = env.getProperty("keycloak.realm");
        String baseUrl = env.getProperty("app.keycloak.users-api-url", Routes.KEYCLOAK_USERS_URI);
        final String url = baseUrl + realm;
        final KeycloakClientRequestFactory factory = new KeycloakClientRequestFactory();
        final KeycloakRestTemplate template = new KeycloakRestTemplate(factory);
        try {
            final UserKeycloak[] users = template.getForObject(url, UserKeycloak[].class);
            if (users == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(users);
        } catch (Exception ex) {
            System.out.println("Impossible de recuperer les users Keycloak: " + ex.getMessage());
            return Collections.emptyList();
        }
    }

    public List<User> getUsers() {
        return this.userRepo.findAll();
    }

    @Transactional(readOnly = true)
    public UserResponse findCurrentUserFromToken() {
        User user = this.kcTools.getCurrentUser();
        if (user == null) {
            throw new NotFoundException("Utilisateur non trouve pour le token courant");
        }

        UserResponse response = this.convertUserToUserResponse(user);
        response.setUserEntreprises(this.userEntrepriseRepo.findByUser(user));
        return response;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findByAll() {
        List<User> users = this.userRepo.findAll();
        List<UserResponse> usersResponses = this.convertUsersToUserResponses(users);
        List<UserKeycloak> usersKeycloak = this.getUsersKeycloak();
        if (!usersKeycloak.isEmpty()) {
            List<UserResponse> usersResponsesKc = this.convertUsersKeycloakToUserResponses(usersKeycloak);
            usersResponses = this.intersectionOfTwoLists(usersResponses, usersResponsesKc);
        }
        return usersResponses;
    }

    private List<UserResponse> convertUsersToUserResponses(List<User> users) {
        return users.stream()
                .map(this::convertUserToUserResponse)
                .collect(Collectors.toList());
    }

    private List<UserResponse> convertUsersKeycloakToUserResponses(List<UserKeycloak> users) {
        List<UserResponse> usersResponses = new ArrayList<>();
        users.forEach(user -> {
            UserResponse ur = new UserResponse();
            ur.setKeycloakId(user.getUsername());
            ur.setUsername(user.getUsername());
            ur.setFname(user.getFirstname());
            ur.setLname(user.getLastname());
            usersResponses.add(ur);
        });
        return usersResponses;
    }

    private UserResponse convertUserToUserResponse(User user) {
        UserResponse ur = new UserResponse();
        UUID id = user.getId();
        ur.setId(id);
        ur.setKeycloakId(user.getKeycloakId());
        ur.setUsername(user.getUsername());
        ur.setFname(user.getFname());
        ur.setLname(user.getLname());
        ur.setCreatedat(user.getCreatedat());
        ur.setUpdatedat(user.getUpdatedat());
        if (id != null) {
            ur.setIntermediaries(this.intermediaryRepo.findAllByUser(id));
            ur.setUserEntreprises(this.userEntrepriseRepo.findByUser(user));
        }
        return ur;
    }

    public List<UserResponse> intersectionOfTwoLists(List<UserResponse> usersDBs, List<UserResponse> usersKCs) {
        List<UserResponse> usersResponses = new ArrayList<>(usersDBs);
        usersKCs.forEach(user -> {
            String keycloakId = user.getKeycloakId();
            Optional<UserResponse> usr = usersResponses.stream()
                    .filter(ur -> ur.getKeycloakId() != null && ur.getKeycloakId().equals(keycloakId))
                    .findFirst();
            if (usr.isEmpty()) {
                usersResponses.add(user);
            }
        });
        return usersResponses;
    }
}
