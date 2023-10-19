package ru.practicum.dto.event;

import lombok.Data;

@Data
public class EventRequestStatusUpdateResult {
    private final ParticipationRequestDto confirmedRequests;
    private final ParticipationRequestDto rejectedRequests;
}