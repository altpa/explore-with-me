package ru.practicum.validation.conflicts;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.model.Event;
import ru.practicum.model.Request;

import java.util.Set;

public interface Conflicts {
    void checkRequestStatus(Set<Request> requests);

    void checkEventRequestsLimit(Event event);

    void checkEventPublished(Long eventId);

    void checkExistsByEventIdAndRequesterId(Long eventId, Long userId);

    void checkUserIsInitiator(Long userId, Event event);

    void checkEventNotPublished(Event event);

    void checkParticipantLimitIsFull(Event event);

    void checkEventCanceled(Event event);

    void checkCategoryName(NewCategoryDto newCategoryDto);

    void checkEventsInCategory(Long catId);

    void checkSameCategory(Long catId, CategoryDto categoryDto);

    void checkUserNameExists(NewUserRequest newUserRequest);
}
