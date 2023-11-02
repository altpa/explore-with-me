package ru.practicum.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.UniIp;

@EnableJpaRepositories
@Transactional
public interface IpRepository extends Repository<UniIp, Long> {
    @Query(value = "SELECT case when count(u)> 0 then true else false end FROM UniIp u WHERE u.ipAddress = :ipAddress AND u.event.id like :eventId")
    boolean existsByIpAddressAndEventId(@Param("ipAddress") String ipAddress, @Param("eventId") Long eventId);

    void save(UniIp uniIp);
}