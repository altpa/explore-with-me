package ru.practicum.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Request;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Transactional
public interface RequestRepository extends Repository<Request, Long> {
    Optional<Request> save(Request request);

    @Query(value = "SELECT r FROM Request r WHERE r.requester.id = ?1 and r.event.id = ?2")
    Streamable<Request> findAllByRequesterAndEventId(long userId, long eventId);

    @Query(value = "SELECT r FROM Request r WHERE r.requester.id = ?1 and r.event.id = ?2")
    Optional<Request> findByRequesterAndEventId(long userId, long eventId);

    Optional<Request> findById(long requestId);

    @Query(value = "select r from Request r where r.requester.id = ?1")
    Streamable<Request>  findByUserId(long eventId);


    @Query(value = "SELECT r FROM Request r WHERE r.id in ids and r.requester.id = ?1 and r.event.id = ?2")
    Streamable<Request> findAllByIdAndRequesterAndEventId(List<Long> ids, long userId, long eventId);
}
