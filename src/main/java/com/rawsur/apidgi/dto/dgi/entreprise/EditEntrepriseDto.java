package com.rawsur.apidgi.dto.dgi.entreprise;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EditEntrepriseDto {
    @NotNull(message = "L'id de l'entreprise ne peut etre nul")
    private UUID id;

    @NotBlank(message = "Le code de l'entreprise ne peut etre nul ou vide")
    private String code;

    @NotBlank(message = "Le nif de l'entreprise ne peut etre nul ou vide")
    private String nif;

    @NotBlank(message = "Le nom de l'entreprise ne peut etre nul ou vide")
    private String nom;
}
