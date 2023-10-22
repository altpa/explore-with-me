package ru.practicum.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class EventFullDto {
    @NotBlank(message = "annotation may not be blank")
    private final String annotation;
    @NotBlank(message = "category may not be blank")
    private final CategoryDto category;
    @Nullable
    @Positive
    private final Long confirmedRequests;
    private final String createdOn;
    private final String description;
    private final String eventDate;
    @Nullable
    @Positive
    private final Long id;
    @NotNull(message = "initiator may not be blank")
    private final UserShortDto initiator;
    @NotNull(message = "location may not be blank")
    private final Location location;
    @NotNull(message = "paid may not be blank")
    private final boolean paid;
    @Nullable
    @Size(max = 10)
    private final Integer participantLimit;
    private final String publishedOn;
    private final boolean requestModeration;
    private final String state;
    @NotBlank(message = "title may not be blank")
    private final String title;
    @Nullable
    @Positive
    private final Long views;
}
