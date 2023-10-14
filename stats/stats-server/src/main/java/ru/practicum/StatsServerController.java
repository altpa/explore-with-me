package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.StatsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsServerController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {

        log.debug("+StatsServerController - getStats: start = {}, end = {}, uri = {}, unique = {}",
                start, end, uris, unique);
        List<ViewStatsDto> answer =  statsService.getStats(start, end, uris, unique);
        log.debug("-StatsServerController - getStats: {}", answer);
        return answer;
    }

    @PostMapping("/hit")
    public HitDto addHit(@RequestBody HitDto hitDto) {
        log.debug("+StatsServerController - addHit: hitDto: app = {}, uri = {}, ip = {}, timestamp = {}",
                hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), hitDto.getTimestamp());
        HitDto answer = statsService.addHit(hitDto);
        log.debug("-StatsServerController - addHit: hitDto: app = {}, uri = {}, ip = {}, timestamp = {}",
                answer.getApp(), answer.getUri(), answer.getIp(), answer.getTimestamp());
        return answer;
    }
}
