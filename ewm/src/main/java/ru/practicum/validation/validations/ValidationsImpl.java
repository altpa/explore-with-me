package ru.practicum.validation.validations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationsImpl implements Validations {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public void checkStartEndTime(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new BadRequestException("rangeStart must be before rangeEnd");
            }
        }
    }

    @Override
    public void checkCategoryExistsById(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new ObjectNotFoundException("catId = " + catId + "not found");
        }
    }

    @Override
    public void checkUserExistsById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("no user with userId = " + userId);
        }
    }

    @Override
    public void checkCompilationExistsById(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new ObjectNotFoundException("Compilation id = " + compId + " not found");
        }
    }

    @Override
    public void checkUserAndCategoryExist(Long userId, Long catId) {
        checkUserExist(userId);
        checkCategoryExist(catId);
        log.debug("-checkUserAndCategoryExist. Pass. userId = {}, catId = {}", userId, catId);
    }

    @Override
    public void checkUserExist(Long userId) {
        log.debug("+checkUserExist. userId = " + userId);
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("userId = " + userId + "not found");
        }
    }

    @Override
    public void checkCategoryExist(Long catId) {
        log.debug("+checkCategoryExist. catId = " + catId);
        if (!categoryRepository.existsById(catId)) {
            throw new ObjectNotFoundException("categoryId = " + catId + "not found");
        }
    }

    @Override
    public void checkUserAndEventExist(Long userId, Long eventId) {
        log.debug("+checkUserAndEventExist. userId = {}, eventId = {}", userId, eventId);
        checkUserExist(userId);
        checkEventExist(eventId);
        log.debug("-checkUserAndEventExist. Pass. userId = {}, eventId = {}", userId, eventId);
    }

    @Override
    public void checkEventExist(Long eventId) {
        log.debug("+checkEventExist. eventId = " + eventId);
        if (!eventRepository.existsById(eventId)) {
            throw new ObjectNotFoundException("eventId = " + eventId + "not found");
        }
        log.debug("-checkEventExist. Pass. eventId = " + eventId);
    }

    @Override
    public void checkUserAndRequestExist(Long userId, Long requestId) {
        log.debug("+checkUserAndRequestExist. userId = {}, eventId = {}", userId, requestId);
        checkUserExist(userId);
        checkRequestExist(requestId);
        log.debug("-checkUserAndRequestExist. Pass. userId = {}, eventId = {}", userId, requestId);
    }

    @Override
    public void checkRequestExist(Long requestId) {
        if (!requestRepository.existsById(requestId)) {
            throw new ObjectNotFoundException("eventId = " + requestId + "not found");
        }
    }
}
