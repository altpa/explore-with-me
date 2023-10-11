package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatsClient extends BaseClient {
    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStats(String start,
                                           String end,
                                           List<String> uris,
                                           Boolean unique) {
        Map<String, Object> parameters = new HashMap<>(Map.of(
                "start", start,
                "end", end,
                "unique", unique.toString()
        ));
        log.info("+StatsClient - getStats: start = {}, end = {}, unique = {}, uris = {}", start, end, unique, uris);

        ResponseEntity<Object> answer;
        if (uris != null) {
            parameters.put("uris", uris);
            log.info("+StatsClient - getStats, uris != null: start = {}, end = {}, unique = {}, uris = {}", start, end, unique, uris);
            answer = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        } else {
            log.info("+StatsClient - getStats, uris == null: start = {}, end = {}, unique = {}, uris = {}", start, end, unique, uris);
            answer = get("/stats?start={start}&end={end}&unique={unique}", parameters);
        }

        log.info("-StatsClient - getStats: answer = {}", answer);
        return answer;
    }

    public ResponseEntity<Object> addHit(HitDto hitDto) {
        return post("/hit", hitDto);
    }
}