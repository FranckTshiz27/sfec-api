package com.rawsur.apidgi.repositories.dgi;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rawsur.apidgi.models.dgi.SfecInvoice;

public interface SfecInvoiceRepo extends JpaRepository<SfecInvoice, UUID> {

    Optional<SfecInvoice> findByExternalInvoiceId(String externalInvoiceId);

    boolean existsByExternalInvoiceId(String externalInvoiceId);

    @Query("SELECT i FROM SfecInvoice i LEFT JOIN FETCH i.certification WHERE i.externalInvoiceId IN :ids")
    List<SfecInvoice> findByExternalInvoiceIdInWithCertification(@Param("ids") Collection<String> ids);

    @Query("SELECT i FROM SfecInvoice i WHERE i.createdAt >= :startDate AND i.createdAt < :endDate ORDER BY i.createdAt DESC")
    List<SfecInvoice> findByCreatedAtPeriod(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);
}
