package ru.practicum.dto.category;

import lombok.Data;
import ru.practicum.dto.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
public class CompilationDto {
    @NotNull(message = "id may not be blank")
    @Positive
    private Long id;
    private Set<EventShortDto> events;
    @NotNull(message = "pinned may not be blank")
    private boolean pinned;
    @NotBlank(message = "title may not be blank")
    private String title;
}
