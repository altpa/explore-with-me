package ru.practicum.service.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.dto.EwmMapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.model.Category;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.UniIp;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.IpRepository;
import ru.practicum.service.admin.AdminService;
import ru.practicum.service.stats.MainStatsService;
import ru.practicum.validation.validations.Validations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
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
    private final IpRepository ipRepository;

    private final AdminService adminService;
    private final MainStatsService mainStatsService;

    private final Validations validations;

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
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation id = " + compId + " not found"));
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
        log.debug("+ getCategoryById. catId = {}", catId);
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
        CategoryDto answer =  mapper.toCategoryDto(category);
        log.debug("- getCategoryById. answer = {}", answer);

        return answer;
    }

    @Override
    public Set<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                        String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                        String sort, Integer from, Integer size, String ipAddress) {
        log.debug("+ getEvents. text = {}, categories = {}, paid = {}, rangeStart={}, rangeEnd = {}, " +
                        "onlyAvailable = {}, sort = {}, from = {}, size = {}, ipAddress = {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, ipAddress);
        PageRequest pageRequest;
        Set<Event> events;
        String sortByDate = "EVENT_DATE";

        LocalDateTime start = (rangeStart != null) ? LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime end = (rangeEnd != null) ? LocalDateTime.parse(rangeEnd, formatter) : null;

        validations.checkStartEndTime(start, end);

        if (sort.equals(sortByDate)) {
            pageRequest = PageRequest.of(from, size, Sort.Direction.DESC, "eventDate");
        } else {
            pageRequest = PageRequest.of(from, size, Sort.Direction.DESC, "views");
        }

        if (text == null && categories == null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAll(pageRequest).stream().collect(Collectors.toSet());
        } else {

            if (onlyAvailable) {
                events = eventRepository.getEventsOnlyAvailable(text, categories, paid,
                        start, end, pageRequest).stream()
                        .map(e -> addHitAndUpdateViews(ipAddress, e))
                        .collect(Collectors.toSet());
            } else {
                log.debug(eventRepository.getEventsNotOnlyAvailable(text, categories, paid,
                        start, end, pageRequest).getContent().toString());
                events = eventRepository.getEventsNotOnlyAvailable(text, categories, paid,
                        start, end, pageRequest).stream()
                        .map(e -> addHitAndUpdateViews(ipAddress, e))
                        .collect(Collectors.toSet());
            }
        }

        Set<EventShortDto> answer = events.stream().map(mapper::toEventShortDto).collect(Collectors.toSet());

        log.debug("- getEvents. answer = {}", answer);

        return answer;
    }

    private Event addHitAndUpdateViews(String ipAddress, Event e) {
        mainStatsService.addHit(new HitDto("EWM", "/events/", ipAddress));
        return increaseViews(ipAddress, e);
    }

    @Override
    public EventFullDto getEventById(long eventId, String ipAddress) {
        log.debug("+ getEventById. eventId = {}, ipAddress = {}", eventId, ipAddress);
        Event event = eventRepository.findPublishedById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("published event not found. eventId = " + eventId));

        event = increaseViews(ipAddress, event);
        mainStatsService.addHit(new HitDto("EWM", "/events/" + eventId, ipAddress));

        EventFullDto answer = mapper.toEventFullDto(event);
        log.debug("- getEvents. answer = {}", answer);

        return answer;
    }

    private Event increaseViews(String ipAddress, Event event) {
        if (!ipRepository.existsByIpAddressAndEventId(ipAddress, event.getId())) {
            log.debug("ipAddress = {} is unique", ipAddress);
            UniIp uniIp = new UniIp();
            uniIp.setIpAddress(ipAddress);
            uniIp.setEvent(event);
            ipRepository.save(uniIp);

            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
            log.debug("eventId = {} increased views = {}", event.getId(), event.getViews());
            return event;
        }
        log.debug("ipAddress = {} not unique", ipAddress);

        return event;
    }
}
