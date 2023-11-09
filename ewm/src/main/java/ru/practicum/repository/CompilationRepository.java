package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Compilation;

import java.util.Optional;

@EnableJpaRepositories
@Transactional
public interface CompilationRepository extends Repository<Compilation, Long> {
    Optional<Compilation> save(Compilation compilation);

    boolean existsById(Long id);

    void deleteById(Long id);

    Optional<Compilation> findById(Long id);

    Page<Compilation> findByPinned(Boolean pinned, Pageable pageable);
}
