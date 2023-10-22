package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MainEventPublicController {

    @GetMapping("/compilations")
    public Set<CompilationDto> getCompilations(@RequestParam boolean pinned,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {

        return null;
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {

        return null;
    }

    @GetMapping("/categories")
    public Set<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {

        return null;
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId) {

        return null;
    }

    @GetMapping("/events")
    public Set<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) boolean paid,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                        @RequestParam(defaultValue = "0") String sort,
                                        @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {

        return null;
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable long id) {

        return null;
    }
}
