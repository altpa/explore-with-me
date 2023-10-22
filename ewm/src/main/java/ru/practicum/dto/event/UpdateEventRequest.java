package ru.practicum.dto.event;

import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.validation.EventDateAfterHours;

import javax.validation.constraints.Size;

@Data
public class UpdateEventRequest {
    private String annotation;

    private Long category;

    private String description;

    private String eventDate;

    private Location location;

    private boolean paid;

    private Integer participantLimit;

    private boolean requestModeration;

    private String stateAction;

    private String title;
}
