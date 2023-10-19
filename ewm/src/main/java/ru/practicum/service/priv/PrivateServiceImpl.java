package ru.practicum.service.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EwmMapper;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.ParticipationRequestDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateServiceImpl implements PrivateService {
    private static final EwmMapper mapper = EwmMapper.INSTANCE;

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Set<EventShortDto> getEventsByUserId(long userId, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto addEventByUserId(long userId, NewEventDto newEventDto) {
        log.debug("+ addEventByUserId. userId: {}. newEventDto: {}", userId, newEventDto);
        EventFullDto eventFullDto;

        if (userRepository.existsById(userId)){
            if (categoryRepository.existsById(newEventDto.getCategory())){
                Event event = eventRepository.save(mapper.toModel(newEventDto)).get();

                event.setLocationLat(newEventDto.getLocation().getLat());
                event.setLocationLong(newEventDto.getLocation().getLon());

                event = eventRepository.save(event).get();

                eventFullDto = mapper.toDto(event);

                eventFullDto.setLocation(newEventDto.getLocation());
            } else {
                throw new ObjectNotFoundException("categoryId = " + newEventDto.getCategory() + "not found");
            }
        } else {
            throw new ObjectNotFoundException("userId = " + userId + "not found");
        }

        log.debug("- addEventByUserId. eventFullDto: {}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto getEventByEventIdAndUserId(long userId, long eventId) {
        return null;
    }

    @Override
    public EventFullDto updateEventByUserIdAndEventId(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        return null;
    }

    @Override
    public Set<ParticipationRequestDto> getRequestByUserIdAndEventId(long userId, long eventId) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateRequest changeRequestStatusByUserIdAndEventId(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return null;
    }

    @Override
    public Set<ParticipationRequestDto> getRequestsByUserId(long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto addRequestByUserId(long userId, long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto cancelRequestToEventIdByUserId(long userId, long eventId) {
        return null;
    }
}
