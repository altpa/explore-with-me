package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import ru.practicum.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> save(User user);
    Optional<User> findById(Long userId);
    Page<User> findByIdIn(List<Long> ids, Pageable pageable);
    void deleteById(long userId);
    Boolean existsById(long userID);
}
