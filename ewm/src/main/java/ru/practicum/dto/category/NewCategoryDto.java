package ru.practicum.dto.category;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewCategoryDto {
    @NotBlank(message = "name may not be blank")
    @Size(min = 1, max = 50)
    private String name;
}
