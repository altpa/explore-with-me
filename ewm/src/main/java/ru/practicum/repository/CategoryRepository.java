package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Category;

import java.util.Optional;

@EnableJpaRepositories
@Transactional
public interface CategoryRepository extends Repository<Category, Long> {
    Optional<Category> save(Category category);

    void deleteById(long catId);

    Optional<Category> findById(long catId);

    boolean existsById(long catId);

    boolean existsByName(String name);

    Page<Category> findAll(Pageable pageable);
}