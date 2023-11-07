package ru.practicum.controller.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.service.stats.MainStatsServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {
    private static final String format ="yyyy-MM-dd HH:mm:ss";

    private final MainStatsServiceImpl service;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam @DateTimeFormat(pattern = format) String start,
                                           @RequestParam @DateTimeFormat(pattern = format) String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {

        log.debug("+StatsController - getStats: start = {}, end = {}, uri = {}, unique = {}", start, end, uris, unique);
        ResponseEntity<Object>  answer =  service.getStats(start, end, uris, unique);
        log.debug("-StatsController - getStats: {}", answer);

        return answer;
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> addHit(@RequestBody HitDto hitDto) {
        log.debug("+StatsController - addHit: hitDto: app = {}, uri = {}, ip = {}, timestamp = {}", hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), hitDto.getTimestamp());
        ResponseEntity<Object> answer = service.addHit(hitDto);
        log.debug("-StatsController - addHit: hitDto: answer = {}", answer);

        return answer;
    }
}