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
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.State;
import ru.practicum.model.StateAction;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

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
        checkUserAndCategoryExist(userId, newEventDto.getCategory());

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
        checkUserExist(userId);
        checkEventExist(eventId);

        Event event = eventRepository.findByIdAndInitiatorId(userId, eventId).get();
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

        checkUserAndEventExist(userId, eventId);
        Event event = eventRepository.findByIdAndStatus(eventId).get();

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
        checkUserAndEventExist(userId, eventId);
        Event event = eventRepository.findById(eventId).get();
        checkEventRequestsLimit(event);
        List<Long> ids = eventRequestStatusUpdateRequest.getRequestIds();
        int requestsQuantity = eventRequestStatusUpdateRequest.getRequestIds().size();
        State status = eventRequestStatusUpdateRequest.getStatus();
        Set<Request> requests = requestRepository.findAllByIdAndRequesterAndEventId(ids, userId, eventId).toSet();
        checkRequestStatus(requests);

        EventRequestStatusUpdateResult answer = getEventRequestStatusUpdateResult(status, requests, event, requestsQuantity);
        log.debug("- changeRequestStatusByUserIdAndEventId. answer:{}", answer);

        return answer;
    }

    private void checkRequestStatus(Set<Request> requests) {
        for (Request request : requests) {
            if (!request.getStatus().equals(PENDING)) {
                throw new ConflictException("Request status is not PENDING. Status = " + request.getStatus());
            }
        }
    }

    private void checkEventRequestsLimit(Event event) {
        log.debug("+ checkEventLimit. event: {}", event);
        long limit = event.getParticipantLimit();
        long requests = event.getConfirmedRequests();
        boolean requestModeration = event.isRequestModeration();
        log.debug("+ checkEventLimit. limit = {}, requests = {}, requestModeration = {}",
                limit, requests, requestModeration);
        if (limit == requests) {
            throw new ConflictException("Participant limit is out. limit = " + limit + ", requests = " + requests);
        }
        if (!requestModeration) {
            throw new ConflictException("No request moderation needed");
        }
    }

    private EventRequestStatusUpdateResult getEventRequestStatusUpdateResult(State status,
                                                                             Set<Request> requests,
                                                                             Event event,
                                                                             int requestsQuantity) {
        log.debug("-+ getEventRequestStatusUpdateResult. status = {}. requests: {}. event: {}. requestsQuantity = {}",
                status, requests, event, requestsQuantity);
        Set<ParticipationRequestDto> confirmedRequests = new HashSet<>(emptySet());
        Set<ParticipationRequestDto> rejectedRequests = new HashSet<>(emptySet());
        for (Request request : requests) {
            if (request.getStatus().equals(PENDING)
                    && event.getParticipantLimit() >= event.getConfirmedRequests()
                    && status.equals(CONFIRMED)) {
                event.setConfirmedRequests(event.getConfirmedRequests() + requestsQuantity);
                confirmedRequests.add(mapper.toParticipationRequestDto(request));
            } else {
                rejectedRequests.add(mapper.toParticipationRequestDto(request));
            }
        }
        Event changedEvent = eventRepository.save(event).get();
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult =
                new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
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
        checkUserAndEventExist(userId, eventId);
        Request request = new Request();
        request.setEvent(eventRepository.findById(eventId).get());
        request.setRequester(userRepository.findById(userId).get());
        log.debug("+ addRequestByUserId. request: {}", request);
        ParticipationRequestDto answer = mapper.toParticipationRequestDto(requestRepository.save(request).get());
        log.debug("- addRequestByUserId. answer: {}", answer);
        return answer;
    }

    @Override
    public ParticipationRequestDto cancelRequestToEventIdByUserId(long userId, long eventId) {
        log.debug("+ cancelRequestToEventIdByUserId. userId = {}, eventId = {}", userId, eventId);
        checkUserAndEventExist(userId, eventId);
        Request request = requestRepository.findByRequesterAndEventId(userId, eventId).get();
        request.setStatus(PENDING);
        ParticipationRequestDto answer = mapper.toParticipationRequestDto(requestRepository.save(request).get());
        log.debug("- addRequestByUserId. answer: {}", answer);

        return answer;
    }

    private void checkUserExist(long userId) {
        log.debug("+checkUserExist. userId = " + userId);
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("userId = " + userId + "not found");
        }
    }

    private void checkCategoryExist(long catId) {
        log.debug("+checkCategoryExist. catId = " + catId);
        if (!categoryRepository.existsById(catId)) {
            throw new ObjectNotFoundException("categoryId = " + catId + "not found");
        }
    }

    private void checkUserAndCategoryExist(long userId, long catId) {
        checkUserExist(userId);
        checkCategoryExist(catId);
        log.debug("-checkUserAndCategoryExist. Pass. userId = {}, catId = {}", userId, catId);
    }

    private void checkUserAndEventExist(long userId, long eventId) {
        log.debug("+checkUserAndEventExist. userId = {}, eventId = {}", userId, eventId);
        checkUserExist(userId);
        checkEventExist(eventId);
        log.debug("-checkUserAndEventExist. Pass. userId = {}, eventId = {}", userId, eventId);
    }
    @Override
    public void checkEventExist(long eventId) {
        log.debug("+checkEventExist. eventId = " + eventId);
        if (!eventRepository.existsById(eventId)) {
            throw new ObjectNotFoundException("eventId = " + eventId + "not found");
        }
        log.debug("-checkEventExist. Pass. eventId = " + eventId);
    }

    private Event setLocationToModel(Event event, Location location) {
        event.setLocationLat(location.getLat());
        event.setLocationLong(location.getLon());

        return eventRepository.save(event).get();
    }

    private EventFullDto setLocationToEventFullDto(EventFullDto eventFullDto, Event event){
        eventFullDto.setLocation(new Location(event.getLocationLat(), event.getLocationLong()));

        return eventFullDto;
    }

    @Override
    public  <T extends UpdateEventRequest> Event prepareEventForUpdate(Event event, T o) {
        log.debug("+ prepareEventForUpdate. event: {}, o: {}", event, o);
        if (o.getAnnotation() != null) {
            event.setAnnotation(o.getAnnotation());
        }
        if (o.getCategory() != null) {
            event.setCategory(categoryRepository.findById(o.getCategory()).get());
        }
        if (o.getDescription() != null) {
            event.setDescription(o.getDescription());
        }
        if (o.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(o.getEventDate(), formatter));
        }
        if (o.getLocation() != null) {
            event.setLocationLat(o.getLocation().getLat());
            event.setLocationLong(o.getLocation().getLon());
        }
        if (o.isPaid()) {
            event.setPaid(true);
        }
        if (o.getParticipantLimit() != null) {
            event.setParticipantLimit(o.getParticipantLimit());
        }
        if (o.isRequestModeration()) {
            event.setRequestModeration(true);
        }
        if (o.getStateAction() != null) {
            if (o.getStateAction().equals(StateAction.PUBLISH_EVENT.toString())) {
                event.setState(PUBLISHED);
            } else if (o.getStateAction().equals(StateAction.CANCEL_REVIEW.toString())) {
                event.setState(CANCELED);
            } else {
                event.setState(PENDING);
            }
        }
        if (o.getTitle() != null) {
            event.setTitle(o.getTitle());
        }
        log.debug("- prepareEventForUpdate. event: {}", event);
        return event;
    }
}
