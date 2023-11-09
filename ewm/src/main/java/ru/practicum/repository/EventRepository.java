package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@EnableJpaRepositories
@Transactional
public interface EventRepository extends Repository<Event, Long> {
    Optional<Event> save(Event event);

    boolean existsById(Long eventId);

    @Query(value = "SELECT e FROM Event e WHERE e.id = :eventId AND e.initiator.id = :userId")
    Optional<Event> findByIdAndInitiator(@Param("eventId")Long eventId, @Param("userId")Long userId);

    Page<Event> findByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findById(Long eventId);

    @Query(value = "SELECT e FROM Event e WHERE e.id in ?1")
    Streamable<Event> findAllById(Set<Long> id);

    @Query(value = "SELECT e FROM Event e WHERE e.id = :eventId and (e.state = 'PENDING' " +
            "or e.state = 'CANCELED')")
    Optional<Event> findPendingOrCanceledById(@Param("eventId") Long eventId);

    @Query(value = "SELECT e FROM Event e WHERE " +
            ":users IS NULL OR e.initiator.id in :users " +
            "AND (:states is null or e.state in :states) " +
            "AND (:categories is null or e.category.id in :categories) " +
            "AND (cast(:rangeStart as date) is null or e.eventDate >= :rangeStart)" +
            "AND (cast(:rangeEnd as date) is null or e.eventDate <= :rangeEnd)"
    )
    Page<Event> getEventsByAdmin(@Param("users") List<Long> users,
                                 @Param("states") List<String> states,
                                 @Param("categories") List<Long> categories,
                                 @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                 Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "((:text IS NULL OR e.annotation LIKE %:text%) OR (:text IS NULL OR e.description LIKE %:text%)) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid is null or e.paid = :paid) " +
            "AND (cast(:rangeStart as date) is null or e.eventDate >= :rangeStart)" +
            "AND (cast(:rangeEnd as date) is null or e.eventDate <= :rangeEnd)" +
            "AND (e.confirmedRequests is null or e.participantLimit >= e.confirmedRequests)")
    Page<Event> getEventsOnlyAvailable(@Param("text") String text,
                                       @Param("categories") List<Long> categories,
                                       @Param("paid") Boolean paid,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "((:text IS NULL OR e.annotation LIKE %:text%) OR (:text IS NULL OR e.description LIKE %:text%)) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid is null or e.paid = :paid) " +
            "AND (cast(:rangeStart as date) is null or e.eventDate >= :rangeStart)" +
            "AND (cast(:rangeEnd as date) is null or e.eventDate <= :rangeEnd)")
    Page<Event> getEventsNotOnlyAvailable(@Param("text") String text,
                                       @Param("categories") List<Long> categories,
                                       @Param("paid") Boolean paid,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Event e SET e.views = e.views + 1 WHERE e.id = :eventId")
    void increaseViewsById(@Param("eventId") Long eventId);

    Page<Event> findAll(Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE e.id = :eventId AND e.state = 'PUBLISHED'")
    Optional<Event> findPublishedById(@Param("eventId") Long eventId);

    Boolean existsByCategoryId(Long catId);
}