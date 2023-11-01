package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "events")
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilations_id"),
            inverseJoinColumns = @JoinColumn(name = "events_id"))
        private Set<Event> events;

    @Column(name = "pinned")
    private boolean pinned;

    @Column(name = "title")
    private String title;

    @Override
    public int hashCode() {
        return Objects.hash(pinned, title);
    }
}
