package ru.practicum.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private Set<ParticipationRequestDto> confirmedRequests;
    private Set<ParticipationRequestDto> rejectedRequests;

    public EventRequestStatusUpdateResult(Set<ParticipationRequestDto> confirmedRequests, Set<ParticipationRequestDto> rejectedRequests) {
    }
}