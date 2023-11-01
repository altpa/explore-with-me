package ru.practicum.dto.event;

import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class EventFullDto {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @NotBlank(message = "annotation may not be blank")
    private String annotation;

    @NotBlank(message = "category may not be blank")
    private CategoryDto category;

    @Nullable
    @Positive
    private Long confirmedRequests = 0L;

    @NotBlank(message = "createdOn may not be blank")
    private String createdOn;

    @NotBlank(message = "description may not be blank")
    private String description;

    @NotBlank(message = "eventDate may not be blank")
    private String eventDate;

    @Nullable
    @Positive
    private Long id;

    @NotNull(message = "initiator may not be blank")
    private UserShortDto initiator;

    @NotNull(message = "location may not be blank")
    private Location location;

    @NotNull(message = "paid may not be blank")
    private boolean paid;

    @Nullable
    @Size(max = 10)
    private Integer participantLimit;

    private String publishedOn;

    private boolean requestModeration;

    private String state;

    @NotBlank(message = "title may not be blank")
    private String title;

    @Nullable
    @Positive
    private Long views = 0L;
}
