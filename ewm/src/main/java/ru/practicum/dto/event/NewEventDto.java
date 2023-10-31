package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.validation.EventDateAfterHours;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NewEventDto {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @NotBlank(message = "annotation may not be blank")
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull(message = "category may not be blank")
    @Positive
    private Long category;

    @NotBlank(message = "description may not be blank")
    @Size(min = 20, max = 7000)
    private String description;

    @NotBlank(message = "eventDate may not be blank")
    @EventDateAfterHours(hours=2)
    private String eventDate;

    @NotNull(message = "location may not be blank")
    private Location location;

    private boolean paid = false;

    private Integer participantLimit = 0;

    private boolean requestModeration = true;

    @NotBlank(message = "title may not be blank")
    @Size(min = 3, max = 120)
    private String title;
}