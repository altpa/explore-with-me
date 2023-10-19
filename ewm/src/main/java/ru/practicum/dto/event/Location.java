package ru.practicum.dto.event;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class Location {
    @Positive
    private final float lat;
    @Positive
    private final float lon;
}
