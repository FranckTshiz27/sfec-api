package com.rawsur.apidgi.dto.dgi.entreprise;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateEntrepriseDto {
    @NotBlank(message = "Le code de l'entreprise ne peut etre nul ou vide")
    private String code;

    @NotBlank(message = "Le nif de l'entreprise ne peut etre nul ou vide")
    private String nif;

    @NotBlank(message = "Le nom de l'entreprise ne peut etre nul ou vide")
    private String nom;
}
