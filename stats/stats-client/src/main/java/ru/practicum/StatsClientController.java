package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
@Validated
public class StatsClientController {
    private final StatsClient statsClient;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam String start,
                                            @RequestParam String end,
                                            @RequestParam(required = false) List<String> uris,
                                            @RequestParam(defaultValue = "false") Boolean unique) {

        log.info("+StatsClientController - getStats: start = {}, end = {}, uri = {}, unique = {}", start, end, uris, unique);
        ResponseEntity<Object>  answer =  statsClient.getStats(start, end, uris, unique);
        log.info("-StatsClientController - getStats: {}", answer);
        return null;
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> addHit(@RequestBody HitDto hitDto) {
        log.info("+StatsClientController - addHit: hitDto: app = {}, uri = {}, ip = {}, timestamp = {}", hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), hitDto.getTimestamp());
        ResponseEntity<Object> answer = statsClient.addHit(hitDto);
        log.info("-StatsClientController - addHit: hitDto: answer = {}", answer);
        return answer;
    }
}