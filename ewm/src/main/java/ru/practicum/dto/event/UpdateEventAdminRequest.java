package ru.practicum.dto.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;
import ru.practicum.validation.EventDateAfterHours;

import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateEventAdminRequest extends UpdateEventRequest {
    @Nullable
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Nullable
    @Size(min = 20, max = 7000)
    private String description;

    @EventDateAfterHours(hours=1)
    private String eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String stateAction;
    
    @Nullable
    @Size(min = 3, max = 120)
    private String title;
}
