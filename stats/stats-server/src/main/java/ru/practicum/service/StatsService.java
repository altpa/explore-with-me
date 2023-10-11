package ru.practicum.service;

import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;

import java.util.List;

public interface StatsService {
    List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique);

    HitDto addHit(HitDto hitDto);
}
