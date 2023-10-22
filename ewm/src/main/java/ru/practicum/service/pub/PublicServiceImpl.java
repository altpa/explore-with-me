package ru.practicum.service.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EwmMapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.model.Category;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.admin.AdminService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {
    private static final EwmMapper mapper = EwmMapper.INSTANCE;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final AdminService adminService;

    @Override
    public Set<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        log.debug("+ getCompilations. pinned = {}. from = {}, size = {}", pinned, from, size);

        Set<CompilationDto> answer = compilationRepository.findByPinned(pinned, PageRequest.of(from, size))
                .stream().map(mapper::toCompilationDto)
                .collect(Collectors.toSet());
        log.debug("- getCompilations. answer = {}", answer);

        return answer;
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        log.debug("+ getCompilationById. compId = {}", compId);
        adminService.checkCompilation(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        log.debug("+ getCompilationById. compilation events = {}", compilation.getEvents().size());
        CompilationDto answer = mapper.toCompilationDto(compilationRepository.findById(compId).get());
        log.debug("- getCompilationById. answer = {}", answer);

        return answer;
    }

    @Override
    public Set<CategoryDto> getCategories(int from, int size) {
        log.debug("+ getCategories. from = {}, size = {}", from, size);
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(from, size));
        Set<CategoryDto> answer = categories.stream().map(mapper::toCategoryDto).collect(Collectors.toSet());
        log.debug("- getCategories. answer = {}", answer);

        return answer;
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        if (categoryRepository.existsById(catId)) {
            log.debug("+ getCategoryById. catId = {}", catId);
            CategoryDto answer =  mapper.toCategoryDto(categoryRepository.findById(catId).get());
            log.debug("- getCategoryById. answer = {}", answer);

            return answer;
        } else {
            throw new ObjectNotFoundException("Category not found");
        }
    }

    @Override
    public Set<EventShortDto> getEvents(String text, List<Long> categories, boolean paid,
                                        String rangeStart, String rangeEnd, boolean onlyAvailable,
                                        String sort, int from, int size) {
        log.debug("+ getEvents. text = {}, categories = {}, paid = {}, rangeStart={}, rangeEnd = {}, " +
                        "onlyAvailable = {}, sort = {}, from = {}, size = {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        PageRequest pageRequest = PageRequest.of(from, size);
        TreeSet<Event> events;
        String sortByDate = "EVENT_DATE";
        String sortByViews = "VIEWS";
        Comparator<Event> comparator;
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (sort.equals(sortByDate)) {
            comparator = Comparator.comparing(Event::getEventDate);
        } else {
            comparator = Comparator.comparing(Event::getViews);
        }
        Supplier<TreeSet<Event>> event = () -> new TreeSet<Event>(comparator);

        if (text == null && categories == null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAll(pageRequest).stream().collect(Collectors.toCollection(event));
        } else {

            if (rangeStart != null) {
                start = LocalDateTime.parse(rangeStart, formatter);
            }
            if (rangeEnd != null) {
                end = LocalDateTime.parse(rangeEnd, formatter);
            }


            if (onlyAvailable) {
                events = eventRepository.getEventsOnlyAvailable(text, categories, paid,
                        start, end, pageRequest).stream().collect(Collectors.toCollection(event));
            } else {
                events = eventRepository.getEventsNotOnlyAvailable(text, categories, paid,
                        start, end, pageRequest).stream().collect(Collectors.toCollection(event));
            }
        }

        Set<EventShortDto> answer = events.stream().map(mapper::toEventShortDto).collect(Collectors.toSet());

        log.debug("- getEvents. answer = {}", answer);

        return answer;
    }

    @Override
    public EventFullDto getEventById(long id) {
        return null;
    }
}
