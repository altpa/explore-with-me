package ru.practicum.dto.complination;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class NewCompilationDto {
    private final Set<Long> events;

    @NotNull
    private final boolean pinned;
    @NotBlank(message = "title may not be blank")
    @Size(min = 1, max = 50)
    private final String title;
}
