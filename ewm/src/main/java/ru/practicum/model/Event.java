package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static ru.practicum.model.State.PENDING;

@Data
@Entity
@Table(name = "events")
public class Event {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

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
    private Long confirmedRequests = 0L;

    @Column(name = "created_on")
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "description")
    @ToString.Exclude
    private String description;

    @Column(name = "event_date")
    @JsonFormat(pattern = DATE_PATTERN)
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
    private String state = PENDING.toString();

    @Column(name = "title")
    private String title;

    @Column(name = "views")
    private Long views = 0L;

    @JsonIgnore
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilation;

    @Override
    public int hashCode() {
        return Objects.hash(annotation, category.getName(), createdOn, eventDate, locationLat, locationLong);
    }
}
