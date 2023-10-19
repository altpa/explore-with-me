package ru.practicum.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class CategoryDto {
    @Nullable
    @Positive
    private Long id;
    @NotBlank(message = "name may not be blank")
    @Size(min = 1, max = 50)
    private String name;
}