package com.rawsur.apidgi.dto.dgi.sfec;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SfecAdditionalTaxDto {

    @NotBlank
    @JsonProperty("tax_code")
    private String taxCode;

    @JsonProperty("tax_label")
    private String taxLabel;

    @NotNull
    @JsonProperty("tax_amount")
    private BigDecimal taxAmount;

    @JsonProperty("tax_rate")
    private BigDecimal taxRate;
}
