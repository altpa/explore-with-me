package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Event;
import ru.practicum.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@EnableJpaRepositories
@Transactional
public interface EventRepository extends Repository<Event, Long> {
    Optional<Event> save(Event event);
    boolean existsById(Long eventId);
    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);
    Page<Event> findByInitiatorId(Long userId, Pageable pageable);
    Optional<Event> findById(Long eventId);

    @Query(value = "SELECT e FROM Event e WHERE e.id in ?1")
    Streamable<Event> findAllById(Set<Long> id);

    @Query(value = "SELECT e FROM Event e WHERE e.id = :eventId and (e.state = ru.practicum.model.State.PENDING or e.state = ru.practicum.model.State.CANCELED)")
    Optional<Event> findByIdAndStatus(@Param("eventId") Long eventId);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "e.initiator.id in :users " +
            "and e.state in :states " +
            "and e.category.id in :categories " +
            "and (e.eventDate BETWEEN :rangeStart AND :rangeEnd)")
    Page<Event> getEventsByAdmin(@Param("users") List<Long> users,
                                 @Param("states") List<State> states,
                                 @Param("categories") List<Long> categories,
                                 @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                 Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "((:text IS NULL OR e.annotation LIKE %:text%) OR (:text IS NULL OR e.description LIKE %:text%)) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid is null or e.paid = :paid) " +
            "AND (:rangeStart is null or e.eventDate >= :rangeStart)" +
            "AND (:rangeEnd is null or e.eventDate <= :rangeEnd)" +
            "AND (e.confirmedRequests is null or e.participantLimit >= e.confirmedRequests)")
    Page<Event> getEventsOnlyAvailable(@Param("text") String text,
                                       @Param("categories") List<Long> categories,
                                       @Param("paid") boolean paid,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "((:text IS NULL OR e.annotation LIKE %:text%) OR (:text IS NULL OR e.description LIKE %:text%)) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid is null or e.paid = :paid) " +
            "AND (:rangeStart is null or e.eventDate >= :rangeStart)" +
            "AND (:rangeEnd is null or e.eventDate <= :rangeEnd)")
    Page<Event> getEventsNotOnlyAvailable(@Param("text") String text,
                                       @Param("categories") List<Long> categories,
                                       @Param("paid") boolean paid,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);

    Page<Event> findAll(Pageable pageable);
}