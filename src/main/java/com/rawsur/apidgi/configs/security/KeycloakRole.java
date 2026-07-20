package com.rawsur.apidgi.configs.security;

public enum KeycloakRole {
  ADMIN("Admin"),
  COURTIER("Courtier"),
  PRODUCTEUR("Producteur"),
  PRODUCTEUR_JUNIOR("Producteur-junior"),
  SUPERVISEUR("Superviseur"),
  CLIENT("Client"),
  CONTROLEUR("Controleur");

  private String value;

  KeycloakRole(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
