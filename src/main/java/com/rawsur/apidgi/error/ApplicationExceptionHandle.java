package com.rawsur.apidgi.error;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;

import com.rawsur.apidgi.exceptions.*;

@RestControllerAdvice
public class ApplicationExceptionHandle {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final Pattern FRAPPE_EXCEPTION_PATTERN = Pattern
      .compile("(?:[A-Za-z]+(?:Error|Exception))\\s*:\\s*([^\\\\\\n\\\"]+)");

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> HandleInvalideArgument(MethodArgumentNotValidException ex) {
    Map<String, String> errorMap = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      errorMap.put(error.getField(), error.getDefaultMessage());
    });
    return errorMap;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public Map<String, String> HandleBusinessException(NotFoundException ex) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("errorMessage", ex.getMessage());
    return errorMap;
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(ConflitException.class)
  public Map<String, String> HandleBusinessException(ConflitException ex) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("errorMessage", ex.getMessage());
    return errorMap;
  }

  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ExceptionHandler(NotAcceptable.class)
  public Map<String, String> HandleBusinessException(NotAcceptable ex) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("errorMessage", ex.getMessage());
    return errorMap;
  }

  @ExceptionHandler(RestClientResponseException.class)
  public ResponseEntity<Map<String, String>> handleRestClientResponseException(RestClientResponseException ex) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("errorMessage", extractReadableFrappeMessage(ex));
    HttpStatus statusCode = HttpStatus.resolve(ex.getRawStatusCode());
    if (statusCode == null) {
      statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return ResponseEntity.status(statusCode).body(errorMap);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(RuntimeException.class)
  public Map<String, String> HandleBusinessException(RuntimeException ex) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("errorMessage", ex.getMessage());
    return errorMap;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(FileNotFoundException.class)
  public Map<String, String> HandleBusinessException(FileNotFoundException ex) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("errorMessage", "Le chemin d’accès spécifié est introuvable");
    return errorMap;
  }

  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ExceptionHandler(DgiException.class)
  public Map<String, String> HandleBusinessException(DgiException ex) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("errorMessage", ex.getMessage());
    return errorMap;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(SfecException.class)
  public Map<String, String> handleSfecException(SfecException ex) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("errorMessage", ex.getMessage());
    return errorMap;
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(SQLException.class)
  public Map<String, String> HandleBusinessException(SQLException ex) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("errMessage", ex.getMessage());
    errorMap.put("ErrorCode", ex.getErrorCode() + "");
    errorMap.put("LocalizedMessage", ex.getLocalizedMessage());
    errorMap.put("SQLState", ex.getSQLState());
    return errorMap;
  }

  private String extractReadableFrappeMessage(RestClientResponseException ex) {
    String body = ex.getResponseBodyAsString();
    if (body == null || body.isBlank()) {
      return "Erreur de validation depuis le service distant.";
    }

    try {
      JsonNode root = OBJECT_MAPPER.readTree(body);

      String directMessage = getTextValue(root, "message");
      if (directMessage != null && !directMessage.isBlank()) {
        return directMessage;
      }

      String serverMessagesRaw = getTextValue(root, "_server_messages");
      String serverMessage = extractServerMessage(serverMessagesRaw);
      if (serverMessage != null && !serverMessage.isBlank()) {
        return serverMessage;
      }

      String exceptionMessage = getTextValue(root, "exception");
      String extractedException = extractFromException(exceptionMessage);
      if (extractedException != null && !extractedException.isBlank()) {
        return extractedException;
      }
    } catch (Exception ignored) {
      // Fallback below when response body is not parsable JSON.
    }

    return "Erreur de validation depuis le service distant.";
  }

  private String extractServerMessage(String serverMessagesRaw) {
    if (serverMessagesRaw == null || serverMessagesRaw.isBlank()) {
      return null;
    }

    try {
      JsonNode serverMessagesNode = OBJECT_MAPPER.readTree(serverMessagesRaw);
      if (!serverMessagesNode.isArray() || serverMessagesNode.isEmpty()) {
        return null;
      }

      JsonNode firstNode = serverMessagesNode.get(0);
      if (firstNode == null || firstNode.isNull()) {
        return null;
      }

      if (firstNode.isTextual()) {
        JsonNode firstNodeAsJson = OBJECT_MAPPER.readTree(firstNode.asText());
        return getTextValue(firstNodeAsJson, "message");
      }

      return getTextValue(firstNode, "message");
    } catch (Exception ignored) {
      return null;
    }
  }

  private String extractFromException(String exceptionMessage) {
    if (exceptionMessage == null || exceptionMessage.isBlank()) {
      return null;
    }
    Matcher matcher = FRAPPE_EXCEPTION_PATTERN.matcher(exceptionMessage);
    if (matcher.find()) {
      return matcher.group(1).trim();
    }
    return null;
  }

  private String getTextValue(JsonNode node, String fieldName) {
    if (node == null || node.path(fieldName).isMissingNode() || node.path(fieldName).isNull()) {
      return null;
    }
    return node.path(fieldName).asText(null);
  }

}
