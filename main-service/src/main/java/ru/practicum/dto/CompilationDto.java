package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

@Data
public class CompilationDto {
    @NotNull(message = "id may not be blank")
    @Positive
    private final Long id;
    private final Set<EventShortDto> events;
    @NotNull(message = "pinned may not be blank")
    private final boolean pinned;
    @NotBlank(message = "title may not be blank")
    private final String title;
}
