package ru.practicum.service.priv;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.ParticipationRequestDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.model.Event;

import java.util.Set;

public interface PrivateService {
    Set<EventShortDto> getEventsByUserId(long userId, int from, int size);

    EventFullDto addEventByUserId(long userId, NewEventDto newEventDto);

    EventFullDto getEventByEventIdAndUserId(long userId, long eventId);

    EventFullDto updateEventByUserIdAndEventId(long userId, long eventId,
                                              UpdateEventUserRequest updateEventUserRequest);

    Set<ParticipationRequestDto> getRequestByUserIdAndEventId(long userId, long eventId);

    EventRequestStatusUpdateResult changeRequestStatusByUserIdAndEventId(long userId, long eventId,
                                                                         EventRequestStatusUpdateRequest
                                                                           eventRequestStatusUpdateRequest);

    //    Закрытый API для работы с запросами текущего пользователя на участие в событиях

    Set<ParticipationRequestDto> getRequestsByUserId(long userId);

    ParticipationRequestDto addRequestByUserId(long userId, long eventId);

    ParticipationRequestDto cancelRequestToEventIdByUserId(long userId, long eventId);

    <T extends UpdateEventRequest> Event prepareEventForUpdate(Event event, T o);
    }
