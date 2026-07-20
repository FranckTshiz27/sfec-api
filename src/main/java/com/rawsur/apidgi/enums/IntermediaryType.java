package com.rawsur.apidgi.enums;

public enum IntermediaryType {
  BANK("Banque"),
  DIRECT_OFFICE("Bureau Direct"),
  BROKER("Courtier"),
  CLIENT("Client");

  String value;

  /**
   * @param value
   */
  IntermediaryType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  
}
