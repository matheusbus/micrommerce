package com.esw.productservice.dto.category;

import com.esw.productservice.validation.NoOffensiveLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    String name
) {
}
