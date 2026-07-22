package com.rawsur.apidgi.dto.dgi.user;

import java.util.List;

import com.rawsur.apidgi.models.dgi.Intermediary;
import com.rawsur.apidgi.models.dgi.User;

import lombok.Data;

@Data
public class UserIntermDto {

  private User user;

  private List<Intermediary> interms;

  private List<UserEntrepriseAssociationDto> entreprises;
}
