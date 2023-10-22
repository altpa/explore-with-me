package ru.practicum.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private final List<Long> requestIds;
    private final String status;
}
