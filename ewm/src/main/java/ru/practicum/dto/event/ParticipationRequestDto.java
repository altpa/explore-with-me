package ru.practicum.dto.event;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Positive;

@Data
public class ParticipationRequestDto {
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
