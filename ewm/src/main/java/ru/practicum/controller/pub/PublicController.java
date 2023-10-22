package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.service.pub.PublicService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {

    private final PublicService pubService;

    @GetMapping("/compilations")
    public Set<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        log.debug("+ getCompilations. pinned = {}. from = {}, size = {}", pinned, from, size);
        Set<CompilationDto> answer = pubService.getCompilations(pinned, from, size);
        log.debug("- getCompilations. answer = {}", answer);

        return answer;
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.debug("+ getCompilationById. compId = {}", compId);
        CompilationDto answer = pubService.getCompilationById(compId);
        log.debug("- getCompilationById. answer = {}", answer);

        return answer;
    }

    @GetMapping("/categories")
    public Set<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.debug("+ getCategories. from = {}, size = {}", from, size);
        Set<CategoryDto> answer =  pubService.getCategories(from, size);
        log.debug("- getCategories. answer = {}", answer);

        return answer;
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.debug("+ getCategoryById. catId = {}", catId);
        CategoryDto answer = pubService.getCategoryById(catId);
        log.debug("- getCategoryById. answer = {}", answer);

        return answer;
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
        log.debug("+ getEvents. text = {}, categories = {}, paid = {}, rangeStart={}, rangeEnd = {}, " +
                "onlyAvailable = {}, sort = {}, from = {}, size = {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        Set<EventShortDto> answer = pubService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size);
        log.debug("- getEvents. answer = {}", answer);
        return answer;
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable long id) {

        return null;
    }
}