package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.complination.NewCompilationDto;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.complination.UpdateCompilationRequest;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.model.State;
import ru.practicum.service.admin.AdminService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/categories")
    @ResponseStatus(CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.debug("+ addCategory. newCategoryDto: {}", newCategoryDto);
        CategoryDto answer = adminService.addCategory(newCategoryDto);
        log.debug("- addCategory. answer: {}", answer);

        return answer;
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(NO_CONTENT)
    public long deleteCategory(@PathVariable long catId) {
        log.debug("+ deleteCategory. catId: {}", catId);
        long answer = adminService.deleteCategory(catId);
        log.debug("- deleteCategory. catId = {} deleted", answer);
        return answer;
    }

    @PatchMapping("/categories/{catId}")
    @ResponseStatus(OK)
    public CategoryDto updateCategory(@PathVariable long catId,
                               @Valid @RequestBody CategoryDto categoryDto) {
        log.debug("+ updateCategory. catId: {}, categoryDto: {}", catId, categoryDto);
        CategoryDto answer = adminService.updateCategory(catId, categoryDto);
        log.debug("- updateCategory. answer: {}", answer);

        return answer;
    }

    @GetMapping("/events")
    @ResponseStatus(OK)
    public Set<EventFullDto> getEvents(@RequestParam List<Long> users,
                                       @RequestParam List<State> states,
                                       @RequestParam List<Long> categories,
                                       @RequestParam String rangeStart,
                                       @RequestParam String rangeEnd,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {

        log.debug("+ getEvents. users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        Set<EventFullDto>  answer = adminService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        log.debug("- getEvents. answer: {}", answer);

        return answer;
    }

    @PatchMapping("/events/{eventId}")
    @ResponseStatus(OK)
    public EventFullDto updateEvent(@PathVariable long eventId,
                                               @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("+ updateEvent. eventId = {}, updateEventAdminRequest: {}", eventId, updateEventAdminRequest);
        EventFullDto answer = adminService.updateEvent(eventId, updateEventAdminRequest);
        log.debug("- updateEvent. answer: {}", answer);

        return answer;
    }

    @GetMapping("/users")
    @ResponseStatus(OK)
    public Set<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                 @RequestParam(defaultValue = "0") int from,
                                 @RequestParam(defaultValue = "10") int size) {
        return adminService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.debug("+ addUser. newUserRequest: {}", newUserRequest);
        UserDto answer = adminService.addUser(newUserRequest);
        log.debug("- addUser. answer: {}", answer);
        return answer;
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(NO_CONTENT)
    public long deleteUser(@PathVariable long userId) {
        log.debug("+ deleteUser. userId = {}", userId);
        long answer = adminService.deleteUser(userId);
        log.debug("- deleteUser. deleted userId = {}", answer);
        return answer;
    }

    @PostMapping("/compilations")
    @ResponseStatus(CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {

        log.debug("+ addCompilation. newCompilationDto: {}", newCompilationDto);
        CompilationDto answer = adminService.addCompilation(newCompilationDto);
        log.debug("- addCompilation. answer: {}", answer);

        return answer;
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(NO_CONTENT)
    public long deleteCompilation(@PathVariable long compId) {
        log.debug("+ deleteCompilation. compId = {}", compId);
        long answer = adminService.deleteCompilation(compId);
        log.debug("- deleteCompilation. deleted compId = {}", answer);
        return answer;
    }

    @PatchMapping("/compilations/{compId}")
    @ResponseStatus(OK)
    public CompilationDto updateCompilations(@PathVariable long compId,
                                    @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {

        log.debug("+ updateCompilations. compId = {}, updateCompilationRequest: {}", compId, updateCompilationRequest);
        CompilationDto answer = adminService.updateCompilations(compId, updateCompilationRequest);
        log.debug("- updateCompilations. answer: {}", answer);

        return answer;
    }
}
