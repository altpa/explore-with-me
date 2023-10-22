package ru.practicum.service.stats;

import org.springframework.http.ResponseEntity;
import ru.practicum.HitDto;

import java.util.List;

public interface MainStatsService {
    ResponseEntity<Object> addHit(HitDto hitDto);

    ResponseEntity<Object> getStats(String start, String end, List<String> uris, boolean unique);
}
