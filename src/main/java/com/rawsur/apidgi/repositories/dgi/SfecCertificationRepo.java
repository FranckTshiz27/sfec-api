package com.rawsur.apidgi.repositories.dgi;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rawsur.apidgi.models.dgi.SfecCertification;

public interface SfecCertificationRepo extends JpaRepository<SfecCertification, UUID> {

    Optional<SfecCertification> findByInvoiceId(String invoiceId);

    Optional<SfecCertification> findByInvoice_Id(UUID invoiceId);
}
