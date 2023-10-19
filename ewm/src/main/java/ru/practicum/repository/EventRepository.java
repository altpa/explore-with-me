package ru.practicum.repository;

import org.springframework.data.repository.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import java.util.Optional;

public interface EventRepository extends Repository<Event, Long> {
    Optional<Event> save(Event event);
}
