package com.rawsur.apidgi.dto.dgi.intermediairy;



import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateIntermediaryDTO {
  @NotBlank(message = "Le code ne peut être nul ou vide")
  private String code;
  @NotBlank(message = "Le nom de l'intermédiaire ne peut être nul ou vide")
  private String name;
  @NotBlank(message = "Le type ne peut être nul ou vide")
  private String type;


}
