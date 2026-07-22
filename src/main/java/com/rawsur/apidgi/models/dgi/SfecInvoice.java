package com.rawsur.apidgi.models.dgi;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.rawsur.apidgi.enums.SfecInvoiceStatus;

import lombok.Data;

@Data
@Entity
@Table(name = "sfec_invoice")
public class SfecInvoice {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String externalInvoiceId;

    /** Code intermediaire Oracle (CODEINTE) a l'origine de la facture. */
    @Column(name = "intermediary_code")
    private Integer intermediaryCode;

    @Column(length = 20)
    private String taxpayerNiu;

    @Column(nullable = false, length = 30)
    private String invoiceType;

    @Column(length = 500)
    private String invoiceSubject;

    private Instant invoiceDueDate;

    @Column(length = 100)
    private String referenceInvoiceId;

    @Column(length = 100)
    private String sciet;

    @Column(length = 100)
    private String terminalIdentifier;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalTaxTAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalTaxRAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalExemptAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalTaxAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal discountAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amountDue;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalLineDiscountAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal additionalCentTax;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal electronicStampDuty;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, length = 20)
    private String recipientType;

    @Column(length = 255)
    private String recipientName;

    @Column(length = 30)
    private String recipientNiu;

    @Column(length = 100)
    private String recipientRccm;

    @Column(length = 500)
    private String recipientAddress;

    @Column(length = 50)
    private String recipientPhone;

    @Column(length = 255)
    private String recipientEmail;

    @Column(nullable = false)
    private Boolean isRecipientTaxable;

    @Column(nullable = false, length = 30)
    private String paymentMethod;

    @Column(length = 100)
    private String paymentReference;

    private Instant paymentDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SfecInvoiceStatus status = SfecInvoiceStatus.DRAFT;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private SfecCertification certification;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SfecAdditionalTax> additionalTaxes = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SfecInvoiceItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
