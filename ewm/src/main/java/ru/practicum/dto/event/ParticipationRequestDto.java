package ru.practicum.dto.event;

import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import javax.validation.constraints.Positive;

@Data
public class ParticipationRequestDto {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final String created;
    @Positive
    @Nullable
    private final Long event;
    @Positive
    @Nullable
    private final Long id;
    @Positive
    @Nullable
    private final Long requester;
    private final String status;
}
