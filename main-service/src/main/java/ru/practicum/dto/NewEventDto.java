package ru.practicum.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class NewEventDto {
    @NotBlank(message = "annotation may not be blank")
    @Size(min = 20, max = 2000)
    private final String annotation;
    @NotNull(message = "category may not be blank")
    @Positive
    private final Long category;
    @NotBlank(message = "description may not be blank")
    @Size(min = 20, max = 7000)
    private final String description;
    @NotBlank(message = "eventDate may not be blank")
    private final String eventDate;
    @NotNull(message = "location may not be blank")
    private final Location location;
    private final boolean paid;
    @Nullable
    @Positive
    private final Integer participantLimit;
    private final boolean requestModeration;
    @NotBlank(message = "title may not be blank")
    @Size(min = 3, max = 120)
    private final String title;
}
