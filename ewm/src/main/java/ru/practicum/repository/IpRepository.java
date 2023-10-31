package ru.practicum.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.UniIp;

@EnableJpaRepositories
@Transactional
public interface IpRepository extends Repository<UniIp, Long> {
    boolean existsByIpAddressAndEventId(String ipAddress, Long eventId);
    void save(UniIp uniIp);
}