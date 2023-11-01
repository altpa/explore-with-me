package ru.practicum.service.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EwmMapper;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.Location;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.ParticipationRequestDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.validation.conflicts.Conflicts;
import ru.practicum.validation.validations.Validations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static ru.practicum.model.State.CANCELED;
import static ru.practicum.model.State.CONFIRMED;
import static ru.practicum.model.State.PENDING;
import static ru.practicum.model.State.PUBLISHED;
import static ru.practicum.model.State.REJECTED;
import static ru.practicum.model.StateAction.CANCEL_REVIEW;
import static ru.practicum.model.StateAction.PUBLISH_EVENT;
import static ru.practicum.model.StateAction.REJECT_EVENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateServiceImpl implements PrivateService {
    private static final EwmMapper mapper = EwmMapper.INSTANCE;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    private final Conflicts conflicts;
    private final Validations validations;

    @Override
    public Set<EventShortDto> getEventsByUserId(long userId, int from, int size) {
        log.debug("+ getEventsByUserId. from = {}, size = {}", from, size);
        Page<Event> events = eventRepository.findByInitiatorId(userId, PageRequest.of(from, size));
        Set<EventShortDto> answer = events.stream().map(mapper::toEventShortDto).collect(Collectors.toSet());
        log.debug("- getEventsByUserId. answer = {}", answer);

        return answer;
    }

    @Override
    public EventFullDto addEventByUserId(long userId, NewEventDto newEventDto) {
        log.debug("+ addEventByUserId. userId: {}. newEventDto: {}", userId, newEventDto);
        validations.checkUserAndCategoryExist(userId, newEventDto.getCategory());

        EventFullDto answer = mapper.toEventFullDto(eventRepository.save(prepareEventForSave(userId, newEventDto)).get());
        answer.setLocation(newEventDto.getLocation());
        log.debug("- addEventByUserId. answer: {}", answer);

        return answer;
    }

    private Event prepareEventForSave(long userId, NewEventDto newEventDto) {
        Event event = mapper.toModel(newEventDto);
        event.setCategory(categoryRepository.findById(newEventDto.getCategory()).get());
        event.setInitiator(userRepository.findById(userId).get());
        event = setLocationToModel(event, newEventDto.getLocation());
        log.debug("+ prepareEventForSave. event: {}", event);
        return event;
    }

    @Override
    public EventFullDto getEventByEventIdAndUserId(long userId, long eventId) {
        log.debug("+ getEventByEventIdAndUserId. userId: {}. eventId: {}", userId, eventId);
        validations.checkUserAndEventExist(userId, eventId);

        Event event = eventRepository.findByIdAndInitiator(eventId, userId).get();
        log.debug("+ getEventByEventIdAndUserId. event: {}", event);
        EventFullDto answer = setLocationToEventFullDto(mapper.toEventFullDto(event), event);
        log.debug("- getEventByEventIdAndUserId. answer: {}", answer);

        return answer;
    }

    @Override
    public EventFullDto updateEventByUserIdAndEventId(long userId, long eventId,
                                                      UpdateEventUserRequest updateEventUserRequest) {
        log.debug("+ updateEventByUserIdAndEventId. userId = {}. eventId = {}. updateEventUserRequest: {}",
                userId, eventId, updateEventUserRequest);

        validations.checkUserAndEventExist(userId, eventId);
        conflicts.checkEventPublished(eventId);

        Event event = eventRepository.findPendingOrCanceledById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("eventId = " + eventId + "not found"));

        event = prepareEventForUpdate(event, updateEventUserRequest);
        event = eventRepository.save(event).get();
        log.debug("+ updateEventByUserIdAndEventId. event: {}", event);
        EventFullDto answer = mapper.toEventFullDto(event);
        log.debug("- updateEventByUserIdAndEventId. answer: {}", answer);

        return answer;
    }

    @Override
    public Set<ParticipationRequestDto> getRequestByUserIdAndEventId(long userId, long eventId) {
        log.debug("+ getRequestByUserIdAndEventId. userId={}, eventId={}", userId, eventId);
        Set<ParticipationRequestDto> answer =
                requestRepository.findAllByRequesterAndEventId(userId, eventId)
                .stream().map(mapper::toParticipationRequestDto).collect(Collectors.toSet());
        log.debug("- getRequestByUserIdAndEventId. answer:{}", answer);

        return answer;
    }

    @Override
    public EventRequestStatusUpdateResult
    changeRequestStatusByUserIdAndEventId(long userId, long eventId,
                                          EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.debug("+ changeRequestStatusByUserIdAndEventId. userId={}, eventId={}, eventRequestStatusUpdateRequest: {}",
                userId, eventId, eventRequestStatusUpdateRequest);

        validations.checkUserExist(userId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("eventId = " + eventId + "not found"));
        conflicts.checkEventRequestsLimit(event);

        List<Long> ids = eventRequestStatusUpdateRequest.getRequestIds();
        int requestsQuantity = eventRequestStatusUpdateRequest.getRequestIds().size();
        String status = eventRequestStatusUpdateRequest.getStatus();

        Set<Request> requests = requestRepository.findByIdIn(ids).toSet();
        conflicts.checkRequestStatus(requests);
        log.debug("+ changeRequestStatusByUserIdAndEventId. requests: {}", requests);

        EventRequestStatusUpdateResult answer = getEventRequestStatusUpdateResult(status, requests, event, requestsQuantity);
        log.debug("- changeRequestStatusByUserIdAndEventId. answer:{}", answer);

        return answer;
    }

    private EventRequestStatusUpdateResult getEventRequestStatusUpdateResult(String status,
                                                                             Set<Request> requests,
                                                                             Event event,
                                                                             int requestsQuantity) {
        log.debug("+ getEventRequestStatusUpdateResult. status = {}. requests: {}. event: {}. requestsQuantity = {}",
                status, requests, event, requestsQuantity);

        Set<ParticipationRequestDto> confirmedRequests = new HashSet<>(emptySet());
        Set<ParticipationRequestDto> rejectedRequests = new HashSet<>(emptySet());

        for (Request request : requests) {
            long confirmedRequestsCount = (event.getConfirmedRequests() == null) ? 0 : event.getConfirmedRequests();
            log.debug("+ getEventRequestStatusUpdateResult. confirmedRequestsCount = {}, participantLimit() = {}, " +
                            "request: {}",
                    confirmedRequestsCount, event.getParticipantLimit(), request);
            if (request.getStatus().equals(PENDING.toString())
                    && event.getParticipantLimit() >= confirmedRequestsCount
                    && status.equals(CONFIRMED.toString())) {
                request.setStatus(CONFIRMED.toString());
                requestRepository.save(request);
                event.setConfirmedRequests(confirmedRequestsCount + 1);
                confirmedRequests.add(mapper.toParticipationRequestDto(request));
                log.debug("+ getEventRequestStatusUpdateResult. confirmedRequests. request: {}", request);
            } else {
                request.setStatus(REJECTED.toString());
                requestRepository.save(request);
                rejectedRequests.add(mapper.toParticipationRequestDto(request));
                log.debug("+ getEventRequestStatusUpdateResult. rejectedRequests. request: {}", request);
            }
        }
        log.debug("+ getEventRequestStatusUpdateResult. event: {}", event);
        Event changedEvent = eventRepository.save(event).get();
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        eventRequestStatusUpdateResult.setConfirmedRequests(confirmedRequests);
        eventRequestStatusUpdateResult.setRejectedRequests(rejectedRequests);
        log.debug("- getEventRequestStatusUpdateResult. eventRequestStatusUpdateResult: {}, changedEvent: {}",
                eventRequestStatusUpdateResult, changedEvent);

        return eventRequestStatusUpdateResult;
    }

    @Override
    public Set<ParticipationRequestDto> getRequestsByUserId(long userId) {
        log.debug("+ getRequestsByUserId. userId={}", userId);
        Set<ParticipationRequestDto> answer = requestRepository.findByUserId(userId)
                .stream().map(mapper::toParticipationRequestDto).collect(Collectors.toSet());
        log.debug("- getRequestsByUserId. answer:{}", answer);

        return answer;
    }

    @Override
    public ParticipationRequestDto addRequestByUserId(long userId, long eventId) {
        log.debug("+ addRequestByUserId. userId = {}, eventId = {}", userId, eventId);

        conflicts.checkExistsByEventIdAndRequesterId(eventId, userId);
        validations.checkUserAndEventExist(userId, eventId);

        Request request = new Request();
        Event event = eventRepository.findById(eventId).get();
        log.debug("participantLimit() = {}, requests = {}, requestModeration = {}",
                event.getParticipantLimit(), requestRepository.countByEventId(eventId), event.isRequestModeration());

        conflicts.checkUserIsInitiator(userId, event);
        conflicts.checkEventNotPublished(event);
        conflicts.checkParticipantLimitIsFull(event);

        request.setEvent(event);
        if (event.getParticipantLimit() == 0) {
            request.setStatus(CONFIRMED.toString());
        }

        log.debug("+ addRequestByUserId. request: {}", request);
        request.setRequester(userRepository.findById(userId).get());
        ParticipationRequestDto answer = mapper.toParticipationRequestDto(requestRepository.save(request).get());
        log.debug("- addRequestByUserId. answer: {}", answer);

        return answer;
    }

    @Override
    public ParticipationRequestDto cancelRequestToEventIdByUserId(long userId, long requestId) {
        log.debug("+ cancelRequestToEventIdByUserId. userId = {}, eventId = {}", userId, requestId);

        validations.checkUserAndRequestExist(userId, requestId);

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("requestId = " + requestId + " not found"));
        request.setStatus(CANCELED.toString());
        ParticipationRequestDto answer = mapper.toParticipationRequestDto(requestRepository.save(request).get());
        log.debug("- cancelRequestToEventIdByUserId. answer: {}", answer);

        return answer;
    }

    private Event setLocationToModel(Event event, Location location) {
        event.setLocationLat(location.getLat());
        event.setLocationLong(location.getLon());

        return eventRepository.save(event).get();
    }

    private EventFullDto setLocationToEventFullDto(EventFullDto eventFullDto, Event event) {
        eventFullDto.setLocation(new Location(event.getLocationLat(), event.getLocationLong()));

        return eventFullDto;
    }

    @Override
    public  <T extends UpdateEventRequest> Event prepareEventForUpdate(Event event, T updateEventRequest) {
        log.debug("+ prepareEventForUpdate. event: {}, o: {}", event, updateEventRequest);
        conflicts.checkEventPublished(event.getId());

        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventRequest.getCategory()).get());
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(), formatter));
        }
        if (updateEventRequest.getLocation() != null) {
            event.setLocationLat(updateEventRequest.getLocation().getLat());
            event.setLocationLong(updateEventRequest.getLocation().getLon());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getStateAction() != null) {
            if (updateEventRequest.getStateAction().equals(PUBLISH_EVENT.toString())) {
                conflicts.checkEventCanceled(event);
                event.setState(PUBLISHED.toString());
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateEventRequest.getStateAction().equals(CANCEL_REVIEW.toString())) {
                event.setState(CANCELED.toString());
            } else if (updateEventRequest.getStateAction().equals(REJECT_EVENT.toString())) {
                event.setState(CANCELED.toString());
            } else {
                    event.setState(PENDING.toString());
                }
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        log.debug("- prepareEventForUpdate. event: {}", event);

        return event;
    }
}
