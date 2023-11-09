package ru.practicum.validation.conflicts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.exception.ConflictException;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static ru.practicum.model.State.CANCELED;
import static ru.practicum.model.State.PENDING;
import static ru.practicum.model.State.PUBLISHED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConflictsImpl implements Conflicts {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public void checkRequestStatus(Set<Request> requests) {
        for (Request request : requests) {
            if (!request.getStatus().equals(PENDING.toString())) {
                throw new ConflictException("Request status is not PENDING. Status = " + request.getStatus());
            }
        }
    }

    @Override
    public void checkEventRequestsLimit(Event event) {
        log.debug("+ checkEventLimit. event: {}", event);
        long limit = event.getParticipantLimit();
        long requestsCount = requestRepository.countConfirmedByEventId(event.getId());
        boolean requestModeration = event.isRequestModeration();
        log.debug("+ checkEventLimit. limit = {}, requestsCount = {}, requestModeration = {}",
                limit, requestsCount, requestModeration);
        if (limit <= requestsCount && requestModeration) {
            throw new ConflictException("Participant limit is out. limit = " + limit + ", requestsCount = " + requestsCount);
        }
        if (!requestModeration) {
            throw new ConflictException("No request moderation needed");
        }
    }

    @Override
    public void checkEventPublished(Long eventId) {
        if (eventRepository.findById(eventId).get().getState().equals(PUBLISHED.toString())) {
            throw new ConflictException("eventId = " + eventId + " already published");
        }
    }

    @Override
    public void checkExistsByEventIdAndRequesterId(Long eventId, Long userId) {
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException("userId = " + userId + " already have request to eventId = " + eventId);
        }
    }

    @Override
    public void checkUserIsInitiator(Long userId, Event event) {
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("userId = " + userId + " initiator of eventId = " + event.getId());
        }
    }

    @Override
    public void checkEventNotPublished(Event event) {
        if (!event.getState().equals(PUBLISHED.toString())) {
            throw new ConflictException("eventId = " + event.getId() + " not published");
        }
    }

    @Override
    public void checkParticipantLimitIsFull(Event event) {
        if (event.getParticipantLimit() <= requestRepository.countByEventId(event.getId()) && !event.isRequestModeration()) {
            throw new ConflictException("eventId = " + event.getId() + " participantLimit is full");
        }
    }

    @Override
    public void checkEventCanceled(Event event) {
        if (event.getState().equals(CANCELED.toString())) {
            throw new ConflictException("eventId = " + event.getId() + " is canceled");
        }
    }

    @Override
    public void checkCategoryName(NewCategoryDto newCategoryDto) {
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("The category name already exist");
        }
    }

    @Override
    public void checkEventsInCategory(Long catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("catId = " + catId + " have events");
        }
    }

    @Override
    public void checkSameCategory(Long catId, CategoryDto categoryDto) {
        Optional<Category> category = categoryRepository.findByName(categoryDto.getName());
        if (category.isPresent() && !category.get().getId().equals(catId)) {
            throw new ConflictException("name = " + categoryDto.getName() + " already exist");
        }
    }

    @Override
    public void checkUserNameExists(NewUserRequest newUserRequest) {
        if (userRepository.existsByName(newUserRequest.getName())) {
            throw new ConflictException("Name = " + newUserRequest.getName() + " already exist");
        }
    }
}
