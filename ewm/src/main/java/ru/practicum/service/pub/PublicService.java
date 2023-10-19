package ru.practicum.service.pub;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;
import java.util.Set;

public interface PublicService {

    Set<CompilationDto> getCompilations(@RequestParam boolean pinned,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size);

    CompilationDto getCompilationById(@PathVariable long compId);

    Set<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size);

    CategoryDto getCategoryById(@PathVariable long catId);

    Set<EventShortDto> getEvents(String text, List<Long> categories, boolean paid, String rangeStart,
                                 String rangeEnd, boolean onlyAvailable, String sort, int from, int size);

    EventFullDto getEventById(long id);
}
