package ru.practicum.service.admin;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CompilationDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.complination.NewCompilationDto;
import ru.practicum.dto.complination.UpdateCompilationRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;

import java.util.List;
import java.util.Set;

public interface AdminService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    long deleteCategory(long catId);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);


    Set<EventFullDto> getEvents(List<Long> users, List<String> states, List<Long> categories,
                                String rangeStart, String rangeEnd, int from, int size);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    CompilationDto updateCompilations(long compId, UpdateCompilationRequest updateCompilationRequest);


    Set<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto addUser(NewUserRequest newUserRequest);

    long deleteUser(long userId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    long deleteCompilation(long compId);
}
