package com.rawsur.apidgi.repositories.dgi;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rawsur.apidgi.models.dgi.Entreprise;
import com.rawsur.apidgi.models.dgi.User;
import com.rawsur.apidgi.models.dgi.UserEntreprise;

public interface UserEntrepriseRepo extends JpaRepository<UserEntreprise, UUID> {
    Optional<UserEntreprise> findByUserAndEntreprise(User user, Entreprise entreprise);

    List<UserEntreprise> findByUser(User user);
}
