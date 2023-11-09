package ru.practicum.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewUserRequest {
    @NotBlank(message = "email may not be blank")
    @Size(min = 6, max = 254)
    @Email
    private final String email;

    @NotBlank(message = "name may not be blank")
    @Size(min = 2, max = 250)
    private final String name;
}
