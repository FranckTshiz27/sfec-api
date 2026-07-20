package com.rawsur.apidgi.models.dgi;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "sfec_certification")
public class SfecCertification {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    private UUID id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sfec_invoice_id", nullable = false, unique = true)
    private SfecInvoice invoice;

    /** invoice_id de la requete SFEC (CreateSfecInvoiceDto.invoiceId). */
    @Column(nullable = false, length = 100)
    private String invoiceId;

    @Column(nullable = false, length = 128)
    private String certificationNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String signature;

    @Column(length = 100)
    private String shortSignature;

    @Column(length = 100)
    private String invoiceNumber;

    private Instant certificationDate;

    @Column(length = 100)
    private String identifier;

    @Column(columnDefinition = "TEXT")
    private String qrCode;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
