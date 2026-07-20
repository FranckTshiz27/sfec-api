package com.rawsur.apidgi.dto.dgi.sfec;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SfecCertificationResponseDto {

    /** Lien vers la requete SFEC : meme valeur que {@code CreateSfecInvoiceDto.invoiceId}. */
    @JsonProperty("invoice_id")
    private String invoiceId;

    @JsonProperty("certification_number")
    private String certificationNumber;

    private String signature;

    @JsonProperty("short_signature")
    private String shortSignature;

    @JsonProperty("invoice_number")
    private String invoiceNumber;

    @JsonProperty("certification_date")
    private Instant certificationDate;

    private String identifier;

    @JsonProperty("qr_code")
    private String qrCode;
}
