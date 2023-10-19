package ru.practicum.service.stats;

import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatsClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainStatsServiceImpl implements MainStatsService {

    private final StatsClient client;

    @Override
    public ResponseEntity<Object> addHit(HitDto hitDto) {
        return client.addHit(hitDto);
    }

    @Override
    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, boolean unique) {
        return client.getStats(start, end, uris, unique);
    }
}