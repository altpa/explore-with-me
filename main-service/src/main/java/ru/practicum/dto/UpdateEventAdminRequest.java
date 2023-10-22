package ru.practicum.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;

@Data
public class UpdateEventAdminRequest {
    @Nullable
    @Size(min = 20, max = 2000)
    private final String annotation;
    private final long category;
    @Nullable
    @Size(min = 20, max = 7000)
    private final String description;
    private final String eventDate;
    private final Location location;
    private final boolean paid;
    private final int participantLimit;
    private final boolean requestModeration;
    private final String stateAction;
    @Nullable
    @Size(min = 3, max = 120)
    private final String title;
}
