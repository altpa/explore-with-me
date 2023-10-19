package ru.practicum.model;

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

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "annotation")
    private String annotation;

    @OneToOne
    @JoinColumn(name = "category")
    @ToString.Exclude
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @OneToOne
    @JoinColumn(name = "initiator")
    @ToString.Exclude
    private User initiator;

    @Column(name = "location_lat")
    private float locationLat;

    @Column(name = "location_long")
    private float locationLong;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "state")
    private State state;

    @Column(name = "title")
    private String title;

    @Column(name = "views")
    private Long views;

    public Event(Long id, String annotation, Category category, Long confirmedRequests,
                 LocalDateTime createdOn, String description, LocalDateTime eventDate, User initiator,
                 float locationLat, float locationLong, boolean paid, Integer participantLimit,
                 LocalDateTime publishedOn, boolean requestModeration, State state, String title,
                 Long views) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
    }

    public Event() {
    }
}
