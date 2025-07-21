package com.neoage.exception;

import com.neoage.constant.ErrorCode;

public final class ProductManagementException extends RuntimeException {

    private ErrorCode errorCode;

    public ProductManagementException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    public ProductManagementException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
