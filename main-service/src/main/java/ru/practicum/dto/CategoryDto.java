package ru.practicum.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class CategoryDto {
    @Nullable
    @Positive
    private final Long id;
    @NotBlank(message = "name may not be blank")
    @Size(min = 1, max = 50)
    private final String name;
}