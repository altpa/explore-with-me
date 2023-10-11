package ru.practicum.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@EnableJpaRepositories
public interface  StatsRepository extends Repository<Hit, Long> {
    @Query(value = "SELECT new ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(h.ip) AS hits) " +
            "FROM Hit h " +
            "WHERE h.timestamp " +
            "BETWEEN :start AND :end " +
            "AND h.uri = :uri " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY hits ASC")
    ViewStatsDto findNotUnique(LocalDateTime start, LocalDateTime end, String uri);

    @Query(value = "SELECT new ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip) AS hits) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri = :uri " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY hits ASC")
    ViewStatsDto findUnique(LocalDateTime start, LocalDateTime end, String uri);

    @Query(value = "SELECT new ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip) AS hits) " +
            "FROM Hit h " +
            "WHERE h.timestamp " +
            "BETWEEN :start AND :end " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY hits DESC")
    List<ViewStatsDto> findAllEmptyUrisUnique(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(h.ip) AS hits) " +
            "FROM Hit h " +
            "WHERE h.timestamp " +
            "BETWEEN :start AND :end " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY hits DESC")
    List<ViewStatsDto> findAllEmptyUrisNotUnique(LocalDateTime start, LocalDateTime end);

    Hit save(Hit hit);
}