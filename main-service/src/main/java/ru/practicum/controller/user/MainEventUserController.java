package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.UpdateEventUserRequest;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
@RestController("/users")
@RequestMapping
@RequiredArgsConstructor
public class MainEventUserController {

//    Закрытый API для работы с событиями

    @GetMapping("/{userId}/events")
    public Set<EventShortDto> getEventsByUserId(@PathVariable long userId,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return null;
    }

    @PostMapping("/{userId}/events")
    public NewEventDto addEventByUserId(@RequestParam long userId,
                                          @RequestParam long eventId) {
        return null;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByEventIdAndUserId(@RequestParam long userId,
                                                   @RequestParam long eventId) {
        return null;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventByUserIdAndEventId(@RequestParam
                                                      long userId,
                                                      @RequestParam
                                                      long eventId,
                                                      @Valid @RequestBody
                                                      UpdateEventUserRequest updateEventUserRequest) {
        return null;
    }


    @GetMapping("/{userId}/events/{eventId}/requests")
    public Set<ParticipationRequestDto> getRequestByUserIdAndEventId(@RequestParam long userId,
                                                                     @RequestParam long eventId) {
        return null;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateRequest changeRequestStatusByUserIdAndEventId(@RequestParam
                                                                                 long userId,
                                                                                 @RequestParam
                                                                                 long eventId,
                                                                                 @Valid @RequestBody
                                                                                 EventRequestStatusUpdateRequest
                                                                                 eventRequestStatusUpdateRequest) {

        return null;
    }

//    Закрытый API для работы с запросами текущего пользователя на участие в событиях

    @GetMapping("{userId}/requests")
    public Set<ParticipationRequestDto> getRequestsByUserId(@PathVariable long userId) {
        return null;
    }

    @PostMapping("{userId}/requests")
    public ParticipationRequestDto addRequestByUserId(@RequestParam long userId,
                                                      @RequestParam long eventId) {
        return null;
    }

    @PatchMapping("{userId}/requests")
    public ParticipationRequestDto cancelRequestToEventIdByUserId(@RequestParam long userId,
                                                                  @RequestParam long eventId) {
        return null;
    }
}