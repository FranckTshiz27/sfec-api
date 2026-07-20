package com.rawsur.apidgi.repositories.dgi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rawsur.apidgi.models.dgi.ExchangeRate;

public interface ExchangeRateRepo extends JpaRepository<ExchangeRate, UUID> {
    boolean existsByEffectiveDateAndSource(LocalDateTime effectiveDate, String source);

    boolean existsByEffectiveDateAndSourceAndIdNot(LocalDateTime effectiveDate, String source, UUID id);

    List<ExchangeRate> findByActiveTrueOrderByEffectiveDateDesc();

    Optional<ExchangeRate> findTopByActiveTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
            LocalDateTime dateTime);
}
