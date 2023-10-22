package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static ru.practicum.model.State.PENDING;

@Data
@Entity
@Table(name = "requests")
public class Request {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "event")
    private Event event;

    @OneToOne
    @JoinColumn(name = "requester")
    private User requester;

    private State status = PENDING;

    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime created = LocalDateTime.now();
}
