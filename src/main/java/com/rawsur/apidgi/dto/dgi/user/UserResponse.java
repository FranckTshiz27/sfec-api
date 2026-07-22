package com.rawsur.apidgi.dto.dgi.user;

import java.util.ArrayList;
import java.util.List;

import com.rawsur.apidgi.models.dgi.Intermediary;
import com.rawsur.apidgi.models.dgi.User;
import com.rawsur.apidgi.models.dgi.UserEntreprise;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends User {
  private List<Intermediary> intermediaries = new ArrayList<>();
  private List<UserEntreprise> userEntreprises = new ArrayList<>();
}
