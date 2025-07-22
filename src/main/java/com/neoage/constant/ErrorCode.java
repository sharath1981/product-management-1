package com.neoage.constant;

import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found"),
  RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),
  INVALID_INPUT(HttpStatus.BAD_REQUEST, "Invalid input"),
  VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),
  INVALID_UUID_FORMAT(HttpStatus.BAD_REQUEST, "Invalid UUID format");

  private final HttpStatus httpStatus;
  private final String message;

  public static ErrorCode byHttpStatus(HttpStatus httpStatus) {
    return Arrays.stream(ErrorCode.values())
        .filter(errorCode -> Objects.equals(httpStatus, errorCode.httpStatus))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
