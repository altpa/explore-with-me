package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.StatsService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsServerController {
    private static final String format ="yyyy-MM-dd HH:mm:ss";

    private final StatsService statsService;

    @GetMapping("/stats")
    @ResponseStatus(OK)
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = format) String start,
                                        @RequestParam @DateTimeFormat(pattern = format) String end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {

        log.debug("+StatsServerController - getStats: start = {}, end = {}, uri = {}, unique = {}",
                start, end, uris, unique);
        List<ViewStatsDto> answer =  statsService.getStats(start, end, uris, unique);
        log.debug("-StatsServerController - getStats: {}", answer);
        return answer;
    }

    @PostMapping("/hit")
    @ResponseStatus(CREATED)
    public HitDto addHit(@RequestBody HitDto hitDto) {
        log.debug("+StatsServerController - addHit: hitDto: app = {}, uri = {}, ip = {}, timestamp = {}",
                hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), hitDto.getTimestamp());
        HitDto answer = statsService.addHit(hitDto);
        log.debug("-StatsServerController - addHit: hitDto: app = {}, uri = {}, ip = {}, timestamp = {}",
                answer.getApp(), answer.getUri(), answer.getIp(), answer.getTimestamp());
        return answer;
    }
}
