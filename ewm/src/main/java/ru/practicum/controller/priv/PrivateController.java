package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.ParticipationRequestDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.service.priv.PrivateService;

import javax.validation.Valid;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateController {

    private final PrivateService privateService;

//    Закрытый API для работы с событиями

    @GetMapping("/{userId}/events")
    public Set<EventShortDto> getEventsByUserId(@PathVariable long userId,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.debug("+ getEventsByUserId. userId = {}. from = {}, size = {}", userId, from, size);
        Set<EventShortDto> answer = privateService.getEventsByUserId(userId, from, size);
        log.debug("- getEventsByUserId. answer: {}", answer);

        return answer;
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(CREATED)
    public EventFullDto addEventByUserId(@PathVariable long userId,
                                        @Valid @RequestBody NewEventDto newEventDto) {
        log.debug("+ addEventByUserId. userId: {}. newEventDto: {}", userId, newEventDto);
        EventFullDto answer = privateService.addEventByUserId(userId, newEventDto);
        log.debug("+ addEventByUserId. answer: {}", answer);

        return answer;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByEventIdAndUserId(@PathVariable long userId,
                                                   @PathVariable long eventId) {
        log.debug("+ getEventByEventIdAndUserId. userId: {}. eventId: {}", userId, eventId);
        EventFullDto answer = privateService.getEventByEventIdAndUserId(userId, eventId);
        log.debug("- getEventByEventIdAndUserId. answer: {}", answer);

        return answer;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(OK)
    public EventFullDto updateEventByUserIdAndEventId(@PathVariable
                                                      long userId,
                                                      @PathVariable
                                                      long eventId,
                                                      @Valid @RequestBody
                                                      UpdateEventUserRequest updateEventUserRequest) {
        log.debug("+ updateEventByUserIdAndEventId. userId = {}. eventId = {}. updateEventUserRequest: {}",
                userId, eventId, updateEventUserRequest);
        EventFullDto answer = privateService.updateEventByUserIdAndEventId(userId, eventId, updateEventUserRequest);
        log.debug("- updateEventByUserIdAndEventId. answer: {}", answer);

        return answer;
    }


    @GetMapping("/{userId}/events/{eventId}/requests")
    public Set<ParticipationRequestDto> getRequestByUserIdAndEventId(@PathVariable long userId,
                                                                     @PathVariable long eventId) {
        log.debug("+ getRequestByUserIdAndEventId. userId={}, eventId={}", userId, eventId);
        Set<ParticipationRequestDto> answer = privateService.getRequestByUserIdAndEventId(userId, eventId);
        log.debug("- getRequestByUserIdAndEventId. answer:{}", answer);

        return answer;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestStatusByUserIdAndEventId(@PathVariable
                                                                                 long userId,
                                                                                 @PathVariable
                                                                                 long eventId,
                                                                                 @Valid @RequestBody
                                                                                 EventRequestStatusUpdateRequest
                                                                                 eventRequestStatusUpdateRequest) {
        log.debug("+ changeRequestStatusByUserIdAndEventId. userId={}, eventId={}, eventRequestStatusUpdateRequest: {}",
                userId, eventId, eventRequestStatusUpdateRequest);
        EventRequestStatusUpdateResult answer = privateService.changeRequestStatusByUserIdAndEventId(userId, eventId, eventRequestStatusUpdateRequest);
        log.debug("- changeRequestStatusByUserIdAndEventId. answer:{}", answer);

        return answer;
    }

//    Закрытый API для работы с запросами текущего пользователя на участие в событиях

    @GetMapping("{userId}/requests")
    public Set<ParticipationRequestDto> getRequestsByUserId(@PathVariable long userId) {
        log.debug("+ getRequestsByUserId. userId={}", userId);
        Set<ParticipationRequestDto> answer = privateService.getRequestsByUserId(userId);
        log.debug("- getRequestsByUserId. answer:{}", answer);

        return answer;
    }

    @PostMapping("{userId}/requests")
    @ResponseStatus(CREATED)
    public ParticipationRequestDto addRequestByUserId(@PathVariable long userId,
                                                      @RequestParam long eventId) {
        log.debug("+ addRequestByUserId. userId={}, eventId={}", userId, eventId);
        ParticipationRequestDto answer = privateService.addRequestByUserId(userId, eventId);
        log.debug("- addRequestByUserId. answer={}", answer);

        return answer;
    }

    @PatchMapping("{userId}/requests")
    public ParticipationRequestDto cancelRequestToEventIdByUserId(@PathVariable long userId,
                                                                  @RequestParam long eventId) {
        log.debug("+ cancelRequestToEventIdByUserId. userId={}, eventId={}", userId, eventId);
        ParticipationRequestDto answer = privateService.cancelRequestToEventIdByUserId(userId, eventId);
        log.debug("- cancelRequestToEventIdByUserId. answer={}", answer);

        return answer;
    }
}
