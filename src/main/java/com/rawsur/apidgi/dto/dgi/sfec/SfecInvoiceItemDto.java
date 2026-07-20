package com.rawsur.apidgi.dto.dgi.sfec;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SfecInvoiceItemDto {

    @NotBlank
    private String designation;

    @JsonProperty("classification_code")
    private String classificationCode;

    @NotNull
    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @NotBlank
    @JsonProperty("discount_type")
    private String discountType;

    @NotNull
    @JsonProperty("net_amount")
    private BigDecimal netAmount;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private BigDecimal subtotal;

    @NotNull
    @JsonProperty("tax_amount")
    private BigDecimal taxAmount;

    @NotBlank
    @JsonProperty("tax_rate")
    private String taxRate;

    @NotNull
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @NotBlank
    private String type;

    @NotNull
    @JsonProperty("unit_price")
    private BigDecimal unitPrice;
}
