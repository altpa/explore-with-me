package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mappers.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.StatsRepository;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsRepository statsRepository;
    private static final HitMapper mapper = HitMapper.INSTANCE;

    @Override
    public List<ViewStatsDto> getStats(String startRequest, String endRequest, List<String>  uris, Boolean unique) {
        LocalDateTime start = LocalDateTime.parse(UriUtils.decode(startRequest, "UTF-8"), formatter);
        LocalDateTime end = LocalDateTime.parse(UriUtils.decode(endRequest, "UTF-8"), formatter);

        log.debug("+StatsServiceImpl - getStats: start = {}, end = {}, uri = {}, unique = {}", start, end, uris, unique);
        List<ViewStatsDto> answer = new ArrayList<>(Collections.emptyList());

        if (uris != null) {
            answer = findWithUri(uris, start, end, unique);
        } else {
            answer = findWithoutUri(start, end, unique);
        }
        log.debug("-StatsServiceImpl - getStats: answer = {}", answer);

//        answer = sortViewStats(answer);
        log.debug("-StatsServiceImpl - getStats: sorted answer = {}", answer);

//        answer.add(new ViewStatsDto("app", "uri", 10L));
        return answer;
    }

    @Override
    public HitDto addHit(HitDto hitDto) {
        log.debug("+StatsServiceImpl - addHit: hitDto = {}", hitDto);
        Hit answer = statsRepository.save(mapper.toModel(hitDto));
        log.debug("-StatsServiceImpl - addHit: answer = {}", answer);

        return mapper.toDto(answer);
    }

    private List<ViewStatsDto> findWithUri(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique) {
        List<ViewStatsDto> answer = new ArrayList<>(Collections.emptyList());

        for (String uri : uris) {
            if (unique) {
                log.debug("+StatsServiceImpl - findWithUri, unique: uri = {}", uri);
                answer.add(statsRepository.findUnique(start, end, uri));
                log.debug("-StatsServiceImpl - findWithUri, unique: answer = {}", answer);
            } else {
                log.debug("+StatsServiceImpl - findWithUri, not unique: uri = {}", uri);
                answer.add(statsRepository.findNotUnique(start, end, uri));
                log.debug("-StatsServiceImpl - findWithUri, not unique: answer = {}", answer);
            }
        }

        return answer;
    }

    private List<ViewStatsDto> findWithoutUri(LocalDateTime start, LocalDateTime end, Boolean unique) {
        List<ViewStatsDto> answer = new ArrayList<>(Collections.emptyList());

        if (unique) {
            answer.addAll(statsRepository.findAllEmptyUrisUnique(start, end));
        } else {
            answer.addAll(statsRepository.findAllEmptyUrisNotUnique(start, end));
        }

        return answer;
    }

    private List<ViewStatsDto> sortViewStats(List<ViewStatsDto> viewStatsDtos) {
        viewStatsDtos.sort(new Comparator<ViewStatsDto>() {
            @Override
            public int compare(ViewStatsDto v1, ViewStatsDto v2) {
                return Long.compare(v2.getHits(), v1.getHits());
            }
        });

        return viewStatsDtos;
    }
}
