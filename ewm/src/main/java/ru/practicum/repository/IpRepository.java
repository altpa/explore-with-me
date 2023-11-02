package ru.practicum.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Event;
import ru.practicum.model.UniIp;

import java.util.Optional;

@EnableJpaRepositories
@Transactional
public interface IpRepository extends Repository<UniIp, Long> {
//    @Query(value = "SELECT case when count(u)> 0 then true else false end FROM UniIp u WHERE u.ipAddress like :ipAddress AND u.event.id = :eventId")
    boolean existsByIpAddressAndEvent(String ipAddress, Event event);

    Optional<UniIp> save(UniIp uniIp);
}