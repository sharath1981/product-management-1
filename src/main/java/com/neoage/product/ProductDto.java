package com.neoage.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProductDto(
    UUID id, @NotBlank String name, String description, @NotNull @Positive Double price) {}
