package ru.practicum.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Request;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Transactional
public interface RequestRepository extends Repository<Request, Long> {
    Optional<Request> save(Request request);

    @Query(value = "SELECT r FROM Request r WHERE r.event.initiator.id = :userId and r.event.id = :eventId")
    Streamable<Request> findAllByRequesterAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query(value = "SELECT r FROM Request r WHERE r.requester.id = :userId and r.event.id = :eventId")
    Optional<Request> findByRequesterAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    Optional<Request> findById(long requestId);

    @Query(value = "SELECT r FROM Request r WHERE r.requester.id = ?1")
    Streamable<Request>  findByUserId(long eventId);

    Streamable<Request> findByIdIn(List<Long> ids);

    Boolean existsByEventIdAndRequesterId(Long eventId, Long userID);

    @Query(value = "SELECT COUNT(*) FROM Request r WHERE r.event.id = ?1 AND r.status = ru.practicum.model.State.CONFIRMED")
    Long countConfirmedByEventId(Long eventId);
    Long countByEventId(Long eventId);
}
