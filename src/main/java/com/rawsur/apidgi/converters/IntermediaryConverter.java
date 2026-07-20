package com.rawsur.apidgi.converters;

import com.rawsur.apidgi.dto.dgi.intermediairy.CreateIntermediaryDTO;
import com.rawsur.apidgi.dto.dgi.intermediairy.UpdateIntermediaryDTO;
import com.rawsur.apidgi.enums.IntermediaryType;
import com.rawsur.apidgi.models.dgi.Intermediary;

public class IntermediaryConverter {
  public static Intermediary convertCreateIntermediaryDtoToIntermediary(CreateIntermediaryDTO intermadiaryDTO) {
    Intermediary intermediary = new Intermediary();
    IntermediaryType intermediaryType =typegetFromChaine(intermadiaryDTO.getType());
    intermediary.setIntermediaryType(intermediaryType.getValue());
    intermediary.setName(intermadiaryDTO.getName());
    intermediary.setCode(intermadiaryDTO.getCode());

    return intermediary;
  }

  public static Intermediary convertUpdateIntermediaryDtoToIntermediary(UpdateIntermediaryDTO intermadiaryDTO) {
    Intermediary intermediary = new Intermediary();
    intermediary.setId(intermadiaryDTO.getId());
    intermediary.setName(intermadiaryDTO.getName());
    intermediary.setCode(intermadiaryDTO.getCode());
    intermediary.setIntermediaryType(intermadiaryDTO.getType());
    return intermediary;
  }

  public static IntermediaryType typegetFromChaine(String chaine) {

    IntermediaryType type = IntermediaryType.DIRECT_OFFICE;

    switch (chaine.toLowerCase()) {
      case "banque":
        type = IntermediaryType.BANK;
        break;

      case "bureau direct":
        type = IntermediaryType.DIRECT_OFFICE;
        break;
      case "client":
        type = IntermediaryType.CLIENT;
        break;

      case "courtier":
        type = IntermediaryType.BROKER;
        break;
    }

    return type;

  }
}
