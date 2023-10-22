package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.User;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Transactional
public interface UserRepository extends Repository<User, Long> {
    Optional<User> save(User user);
    Optional<User> findById(Long userId);
    Page<User> findByIdIn(List<Long> ids, Pageable pageable);
    void deleteById(long userId);
    Boolean existsById(long userID);
}
