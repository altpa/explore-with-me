package ru.practicum.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

@Data
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private Set<ParticipationRequestDto> confirmedRequests = Collections.emptySet();
    private Set<ParticipationRequestDto> rejectedRequests = Collections.emptySet();
}