package ru.practicum;

import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final StatsClient client;

    public ResponseEntity<Object> addHit(HitDto hitDto) {
        return client.addHit(hitDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, boolean unique) {
        return client.getStats(start, end, uris, unique);
    }
}