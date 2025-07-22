package com.neoage.validator;

import com.neoage.constant.ErrorCode;
import com.neoage.exception.ProductManagementException;
import jakarta.validation.Validator;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ValidatorUtil {

  private final Validator validator;

  public ValidatorUtil(Validator validator) {
    this.validator = validator;
  }

  public <T> Mono<T> validate(T target) {
    final var violations = validator.validate(target);
    if (!violations.isEmpty()) {
      final var message =
          violations.stream()
              .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
              .collect(Collectors.joining(", "));
      return Mono.error(new ProductManagementException(ErrorCode.VALIDATION_FAILED, message));
    }
    return Mono.just(target);
  }
}
