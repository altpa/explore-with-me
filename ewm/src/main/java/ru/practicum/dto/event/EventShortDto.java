package ru.practicum.dto.event;

import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class EventShortDto {
    @NotBlank(message = "annotation may not be blank")
    private final String annotation;
    @NotNull(message = "category may not be blank")
    private final CategoryDto category;
    @Nullable
    @Positive
    private final Long confirmedRequests;
    @NotBlank(message = "eventDate may not be blank")
    private final String eventDate;
    @Nullable
    @Positive
    private final Long id;
    @NotNull(message = "initiator may not be blank")
    private final UserShortDto initiator;
    @NotNull(message = "paid may not be blank")
    private final boolean paid;
    @NotBlank(message = "title may not be blank")
    private final String title;
    @Nullable
    @Positive
    private final Long views;
}
