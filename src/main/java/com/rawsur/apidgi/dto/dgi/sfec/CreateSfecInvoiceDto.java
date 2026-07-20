package com.rawsur.apidgi.dto.dgi.sfec;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateSfecInvoiceDto {

    @NotBlank
    @JsonProperty("invoice_id")
    private String invoiceId;

    @Size(max = 20)
    @JsonProperty("taxpayer_niu")
    private String taxpayerNiu;

    @NotBlank
    @Pattern(regexp = "salesInvoice|creditNote", message = "invoice_type doit etre salesInvoice ou creditNote")
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

    @NotNull
    private BigDecimal subtotal;

    @NotNull
    @JsonProperty("total_tax_t_amount")
    private BigDecimal totalTaxTAmount;

    @NotNull
    @JsonProperty("total_tax_r_amount")
    private BigDecimal totalTaxRAmount;

    @NotNull
    @JsonProperty("total_exempt_amount")
    private BigDecimal totalExemptAmount;

    @NotNull
    @JsonProperty("total_tax_amount")
    private BigDecimal totalTaxAmount;

    @NotNull
    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @NotNull
    @JsonProperty("amount_due")
    private BigDecimal amountDue;

    @NotNull
    @JsonProperty("total_line_discount_amount")
    private BigDecimal totalLineDiscountAmount;

    @NotNull
    @JsonProperty("additional_cent_tax")
    private BigDecimal additionalCentTax;

    @NotNull
    @JsonProperty("electronic_stamp_duty")
    private BigDecimal electronicStampDuty;

    @NotNull
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @NotBlank
    private String currency;

    @NotBlank
    @Pattern(regexp = "business|individual|government|foreign", message = "recipient_type invalide")
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

    @NotNull
    @JsonProperty("is_recipient_taxable")
    private Boolean isRecipientTaxable;

    @NotBlank
    @Pattern(regexp = "bank_transfer|card|cash|mobile_money|cheque", message = "payment_method invalide")
    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("payment_reference")
    private String paymentReference;

    @JsonProperty("payment_date")
    private Instant paymentDate;

    private String notes;

    @Valid
    @JsonProperty("additional_taxes")
    private List<SfecAdditionalTaxDto> additionalTaxes = new ArrayList<>();

    @Valid
    @NotNull
    private List<SfecInvoiceItemDto> items = new ArrayList<>();
}
