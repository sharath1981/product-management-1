package com.neoage.exception;

import com.neoage.constant.ErrorCode;
import lombok.Getter;

@Getter
public final class ProductManagementException extends RuntimeException {

  private final ErrorCode errorCode;

  public ProductManagementException(ErrorCode errorCode) {
    this(errorCode, errorCode.getMessage());
  }

  public ProductManagementException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
