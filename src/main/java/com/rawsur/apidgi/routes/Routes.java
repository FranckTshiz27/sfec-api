package com.rawsur.apidgi.routes;

public interface Routes {
  public static String EXCHANGE_RATE_BASE_URI = "exchange-rate";
  public static String ENTREPRISE_BASE_URI = "entreprise";
  public static String INTERMEDIAIRY_URI = "/intermediary";
  public static String SFEC_INVOICE_BASE_URI = "invoices";
  public static String RAWSUR_INVOICES_TYPE_BASE_URI = "rawsur-invoice";
  public static String CREATE_DGI_INVOICE = "/create-dgi-invoice";
  public static String GET_DGI_INVOICE = "/invoices";
  public static String USER_URI = "/user";
  public static String KEYCLOAK_USERS_URI = "http://192.168.88.82:9006/api/keycloak/user/v1/get-by-realm/";
}
