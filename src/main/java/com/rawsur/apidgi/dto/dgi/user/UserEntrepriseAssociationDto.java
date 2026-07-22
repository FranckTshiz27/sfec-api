package com.rawsur.apidgi.dto.dgi.user;

import java.util.UUID;

import lombok.Data;

@Data
public class UserEntrepriseAssociationDto {
    private UUID idEntreprise;
    private Boolean isActive;
}
