package ru.practicum.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Compilation;

@EnableJpaRepositories
@Transactional
public interface CompilationRepository extends Repository<Compilation, Long> {
    Compilation save(Compilation compilation);
}
