package com.rawsur.apidgi.dto.dgi.exchangeRate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EditExchangeRateDto {
    @NotNull(message = "L'id est obligatoire")
    private UUID id;

    @NotNull(message = "Le taux est obligatoire")
    @DecimalMin(value = "0.0000001", inclusive = true, message = "Le taux doit etre superieur a 0")
    private BigDecimal rate;

    @NotNull(message = "La date d'effet est obligatoire")
    private LocalDateTime effectiveDate;

    @Size(max = 100, message = "La source ne doit pas depasser 100 caracteres")
    private String source = "DGI";

    @NotNull(message = "Le statut actif est obligatoire")
    private Boolean active = true;
}
