package ru.practicum.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class UserShortDto {
    @Positive
    private final long id;
    @NotBlank(message = "name may not be blank")
    private final String name;
}
