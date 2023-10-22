package ru.practicum.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class UpdateEventUserRequest {
    @Nullable
    @Size(min = 20, max = 2000)
    private final String annotation;
    @Positive
    @Nullable
    private final Long category;
    @Nullable
    @Size(min = 20, max = 7000)
    private final String description;
    private final String eventDate;
    private final Location location;
    private final boolean paid;
    @Positive
    @Nullable
    private final Integer participantLimit;
    private final boolean requestModeration;
    private final String stateAction;
    @Nullable
    @Size(min = 3, max = 120)
    private final String title;
}
