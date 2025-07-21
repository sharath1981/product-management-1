package com.neoage.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductDto(UUID id,
                         @NotBlank String name,
                         String description,
                         @NotNull @Positive Double price) {
}
