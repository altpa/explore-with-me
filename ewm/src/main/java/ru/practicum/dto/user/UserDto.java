package ru.practicum.dto.user;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class UserDto {
    @NotBlank(message = "email may not be blank")
    @Email(message = "Email is not valid", regexp = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`" +
            "{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private final String email;
    @Nullable
    @Positive
    private final Long id;
    @NotBlank(message = "name may not be blank")
    private final String name;
}