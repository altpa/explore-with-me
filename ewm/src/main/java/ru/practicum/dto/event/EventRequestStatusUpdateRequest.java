package ru.practicum.dto.event;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private final List<Long> requestIds;
    private final String status;
}
