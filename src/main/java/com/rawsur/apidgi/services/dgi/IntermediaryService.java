package com.rawsur.apidgi.services.dgi;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rawsur.apidgi.configs.security.KeycloakRole;
import com.rawsur.apidgi.converters.IntermediaryConverter;
import com.rawsur.apidgi.dto.dgi.intermediairy.CreateIntermediaryDTO;
import com.rawsur.apidgi.enums.IntermediaryType;
import com.rawsur.apidgi.exceptions.NotAcceptable;
import com.rawsur.apidgi.models.dgi.Intermediary;
import com.rawsur.apidgi.models.dgi.User;
import com.rawsur.apidgi.repositories.dgi.IntermediaryRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IntermediaryService {

  @Autowired
  private final IntermediaryRepo intermediaryRepo;

  @Autowired
  private KeycloakTools kcTools;

  @Transactional
  public Intermediary create(CreateIntermediaryDTO intermediaryDTO) {
    this.kcTools.getCurrentUser();
    Intermediary intermediary = new Intermediary();

    IntermediaryType intermediaryType = IntermediaryConverter.typegetFromChaine(intermediaryDTO.getType());
    intermediary.setIntermediaryType(intermediaryType.getValue().trim());
    intermediary.setName(intermediaryDTO.getName().trim());
    String code = intermediaryDTO.getCode().trim();
    intermediary.setCode(code);

    intermediary = this.intermediaryRepo.save(intermediary);

    return intermediary;

  }

  @Transactional
  public Intermediary update(Intermediary intermediaryIn) {
    this.kcTools.getCurrentUser();

    intermediaryIn.getCode().trim();
    intermediaryIn.getIntermediaryType().trim();
    intermediaryIn.getName().trim();

    Intermediary savedIntermediaryIn = this.intermediaryRepo.save(intermediaryIn);
    return savedIntermediaryIn;
  }

  public Intermediary getByCode(String code) {
    return this.intermediaryRepo.findByCode(code);
  }

  @Transactional
  public Intermediary findById(UUID id) {
    return this.intermediaryRepo.findById(id).orElse(null);
  }

  @Transactional(readOnly = true)
  public List<Intermediary> findByAll() {
    if (isCurrentUserAdmin()) {
      return this.intermediaryRepo.findAll();
    }

    return findIntermediariesForCurrentUser();
  }

  /**
   * Codes intermediaires autorises pour l'utilisateur courant.
   * {@code null} = aucun filtre (Admin), liste vide = aucun intermediaire associe.
   */
  @Transactional(readOnly = true)
  public List<Integer> getAllowedIntermediaryCodes() {
    if (isCurrentUserAdmin()) {
      return null;
    }

    return findIntermediariesForCurrentUser().stream()
        .map(Intermediary::getCode)
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(code -> !code.isEmpty())
        .map(code -> {
          try {
            return Integer.valueOf(code);
          } catch (NumberFormatException ex) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .distinct()
        .collect(Collectors.toList());
  }

  public void assertCanAccessIntermediary(Integer codeInte) {
    List<Integer> allowedCodes = getAllowedIntermediaryCodes();
    if (allowedCodes == null) {
      return;
    }
    if (codeInte == null || !allowedCodes.contains(codeInte)) {
      throw new NotAcceptable("Vous n'avez pas acces aux transactions de cet intermediaire");
    }
  }

  private List<Intermediary> findIntermediariesForCurrentUser() {
    User user = this.kcTools.getCurrentUser();
    if (user == null || user.getId() == null) {
      return Collections.emptyList();
    }
    return this.intermediaryRepo.findAllByUser(user.getId());
  }

  private boolean isCurrentUserAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getAuthorities() == null) {
      return false;
    }

    Set<String> authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());

    String adminRole = KeycloakRole.ADMIN.getValue();
    return authorities.contains(adminRole) || authorities.contains("ROLE_" + adminRole);
  }

}
