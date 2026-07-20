package com.rawsur.apidgi.converters;

import com.rawsur.apidgi.dto.dgi.entreprise.CreateEntrepriseDto;
import com.rawsur.apidgi.dto.dgi.entreprise.EditEntrepriseDto;
import com.rawsur.apidgi.models.dgi.Entreprise;

public class EntrepriseConverter {

    public static Entreprise toEntreprise(CreateEntrepriseDto createEntrepriseDto) {
        Entreprise entreprise = new Entreprise();
        entreprise.setCode(createEntrepriseDto.getCode().trim().toUpperCase());
        entreprise.setNif(createEntrepriseDto.getNif().trim().toUpperCase());
        entreprise.setNom(createEntrepriseDto.getNom().trim());
        return entreprise;
    }

    public static Entreprise toEntreprise(EditEntrepriseDto editEntrepriseDto) {
        Entreprise entreprise = new Entreprise();
        entreprise.setId(editEntrepriseDto.getId());
        entreprise.setCode(editEntrepriseDto.getCode().trim().toUpperCase());
        entreprise.setNif(editEntrepriseDto.getNif().trim().toUpperCase());
        entreprise.setNom(editEntrepriseDto.getNom().trim());
        return entreprise;
    }
}
