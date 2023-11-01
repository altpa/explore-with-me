package ru.practicum.validation.validations;

import java.time.LocalDateTime;

public interface Validations {
    void checkStartEndTime(LocalDateTime start, LocalDateTime end);

    void checkCategoryExistsById(Long catId);

    void checkUserExistsById(Long userId);

    void checkCompilationExistsById(Long compId);

    void checkUserAndCategoryExist(Long userId, Long catId);

    void checkUserExist(Long userId);

    void checkCategoryExist(Long catId);

    void checkRequestExist(Long requestId);

    void checkUserAndEventExist(Long userId, Long eventId);

    void checkUserAndRequestExist(Long userId, Long eventId);

    void checkEventExist(Long eventId);
}
