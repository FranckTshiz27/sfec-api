package com.rawsur.apidgi.repositories.dgi;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rawsur.apidgi.models.dgi.Entreprise;

public interface EntrepriseRepo extends JpaRepository<Entreprise, UUID> {
    Optional<Entreprise> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, UUID id);

    boolean existsByNif(String nif);

    boolean existsByNifAndIdNot(String nif, UUID id);
}
