package com.rawsur.apidgi.dto.dgi.sfec;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SfecInvoiceResponseDto {

    private UUID id;

    @JsonProperty("invoice_id")
    private String invoiceId;

    @JsonProperty("taxpayer_niu")
    private String taxpayerNiu;

    @JsonProperty("invoice_type")
    private String invoiceType;

    @JsonProperty("invoice_subject")
    private String invoiceSubject;

    @JsonProperty("invoice_due_date")
    private Instant invoiceDueDate;

    @JsonProperty("reference_invoice_id")
    private String referenceInvoiceId;

    private String sciet;

    @JsonProperty("terminal_identifier")
    private String terminalIdentifier;

    private BigDecimal subtotal;

    @JsonProperty("total_tax_t_amount")
    private BigDecimal totalTaxTAmount;

    @JsonProperty("total_tax_r_amount")
    private BigDecimal totalTaxRAmount;

    @JsonProperty("total_exempt_amount")
    private BigDecimal totalExemptAmount;

    @JsonProperty("total_tax_amount")
    private BigDecimal totalTaxAmount;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @JsonProperty("amount_due")
    private BigDecimal amountDue;

    @JsonProperty("total_line_discount_amount")
    private BigDecimal totalLineDiscountAmount;

    @JsonProperty("additional_cent_tax")
    private BigDecimal additionalCentTax;

    @JsonProperty("electronic_stamp_duty")
    private BigDecimal electronicStampDuty;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    private String currency;

    @JsonProperty("recipient_type")
    private String recipientType;

    @JsonProperty("recipient_name")
    private String recipientName;

    @JsonProperty("recipient_niu")
    private String recipientNiu;

    @JsonProperty("recipient_rccm")
    private String recipientRccm;

    @JsonProperty("recipient_address")
    private String recipientAddress;

    @JsonProperty("recipient_phone")
    private String recipientPhone;

    @JsonProperty("recipient_email")
    private String recipientEmail;

    @JsonProperty("is_recipient_taxable")
    private Boolean isRecipientTaxable;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("payment_reference")
    private String paymentReference;

    @JsonProperty("payment_date")
    private Instant paymentDate;

    private String notes;

    @JsonProperty("additional_taxes")
    private List<SfecAdditionalTaxDto> additionalTaxes = new ArrayList<>();

    private List<SfecInvoiceItemDto> items = new ArrayList<>();

    private String status;

    @JsonProperty("intermediary_code")
    private Integer intermediaryCode;

    @JsonProperty("certification_number")
    private String certificationNumber;

    private String signature;

    @JsonProperty("short_signature")
    private String shortSignature;

    @JsonProperty("qr_code")
    private String qrCode;

    @JsonProperty("certification_date")
    private Instant certificationDate;

    @JsonProperty("invoice_number")
    private String invoiceNumber;

    private String identifier;

    @JsonProperty("created_at")
    private Instant createdAt;
}
