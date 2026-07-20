package com.rawsur.apidgi.exceptions;

import lombok.Getter;

@Getter
public class ConflitException extends RuntimeException {
  private int code;

  public ConflitException(String message) {
    super(message);
  }

  public ConflitException(String message, int code) {
    super(message);
    this.code = code;
  }
}
