package com.esw.productservice.dto.product;

import com.esw.productservice.validation.NoOffensiveLanguage;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductUpdateRequest(
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    String name,

    @Size(max = 255, message = "Description can't exceed 255 characters")
    String description,

    @NotNull(message = "Price is required")
    BigDecimal price,

    @NotNull(message = "Category ID is required")
    Long categoryId
) {
}
