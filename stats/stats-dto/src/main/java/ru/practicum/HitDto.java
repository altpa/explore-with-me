package ru.practicum;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class HitDto {
    private int id;

    @NotBlank(message = "app may not be blank")
    private String app;

    @NotBlank(message = "uri may not be blank")
    private String uri;

    @NotBlank(message = "ip may not be blank")
    private String ip;

    @NotBlank(message = "timestamp may not be blank")
    private String timestamp;

    public HitDto(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
    }
}
