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
    public Set<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                       @RequestParam(required = false) List<String> states,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) String rangeStart,
                                       @RequestParam(required = false) String rangeEnd,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {

        return adminService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId,
                                               @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {

        return adminService.updateEvent(eventId, updateEventAdminRequest);
    }

    @GetMapping("/users")
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
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {

        return adminService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        adminService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public EventFullDto updateEvent(@PathVariable long compId,
                                    @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {

        return adminService.updateEvent(compId, updateCompilationRequest);
    }
}
