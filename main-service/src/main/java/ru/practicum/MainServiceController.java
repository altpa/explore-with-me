package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MainServiceController {

    private final MainService service;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {

        log.info("+MainServiceController - getStats: start = {}, end = {}, uri = {}, unique = {}", start, end, uris, unique);
        ResponseEntity<Object>  answer =  service.getStats(start, end, uris, unique);
        log.info("-MainServiceController - getStats: {}", answer);
        return null;
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> addHit(@RequestBody HitDto hitDto) {
        log.info("+MainServiceController - addHit: hitDto: app = {}, uri = {}, ip = {}, timestamp = {}", hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), hitDto.getTimestamp());
        ResponseEntity<Object> answer = service.addHit(hitDto);
        log.info("-MainServiceController - addHit: hitDto: answer = {}", answer);
        return answer;
    }
}