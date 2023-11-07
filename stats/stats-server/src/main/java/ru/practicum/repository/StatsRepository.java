package ru.practicum.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@EnableJpaRepositories
public interface  StatsRepository extends Repository<Hit, Long> {
    @Query(value = "SELECT h.app, h.uri, COUNT(h.ip) AS hits " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY hits ASC")
    List<ViewStatsDto> findNotUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip) AS hits) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY hits ASC")
    List<ViewStatsDto> findUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip) AS hits) " +
            "FROM Hit h " +
            "WHERE cast(h.timestamp as date) " +
            "BETWEEN cast(:start as date) AND cast(:end as date) " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY hits DESC")
    List<ViewStatsDto> findAllEmptyUrisUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(h.ip) AS hits) " +
            "FROM Hit h " +
            "WHERE cast(h.timestamp as date) " +
            "BETWEEN cast(:start as date) AND cast(:end as date) " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY hits DESC")
    List<ViewStatsDto> findAllEmptyUrisNotUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Hit save(Hit hit);
}