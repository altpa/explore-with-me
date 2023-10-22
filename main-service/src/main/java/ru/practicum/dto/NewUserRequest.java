package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewUserRequest {
    @NotBlank(message = "email may not be blank")
    private final String email;
    @NotBlank(message = "name may not be blank")
    private final String name;
}
