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
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MainEventAdminController {

    @PostMapping("/categories")
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {

        return null;
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable long catId) {

    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable long catId,
                               @Valid @RequestBody CategoryDto categoryDto) {

        return null;
    }

    @GetMapping("/events")
    public Set<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                       @RequestParam(required = false) List<String> states,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) String rangeStart,
                                       @RequestParam(required = false) String rangeEnd,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {

        return null;
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId,
                                               @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {

        return null;
    }

    @GetMapping("/users")
    public Set<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                 @RequestParam(defaultValue = "0") String from,
                                 @RequestParam(defaultValue = "10") String size) {
        return null;
    }

    @PostMapping("/users")
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {

        return null;
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable long userId) {

    }

    @PostMapping("/compilations")
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {

        return null;
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable long compId) {

    }

    @PatchMapping("/compilations/{compId}")
    public EventFullDto updateEvent(@PathVariable long compId,
                                    @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {

        return null;
    }
}
