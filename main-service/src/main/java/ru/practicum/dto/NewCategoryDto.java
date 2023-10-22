package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewCategoryDto {
    @NotBlank(message = "name may not be blank")
    @Size(min = 1, max = 50)
    private final String name;
}
