package ru.practicum.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EwmMapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CompilationDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.complination.NewCompilationDto;
import ru.practicum.dto.complination.UpdateCompilationRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.Location;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.priv.PrivateService;
import ru.practicum.validation.conflicts.Conflicts;
import ru.practicum.validation.validations.Validations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final PrivateService privateService;
    private static final EwmMapper mapper = EwmMapper.INSTANCE;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Conflicts conflicts;
    private final Validations validations;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        log.debug("+ addCategory. newCategoryDto: {}", newCategoryDto.toString());
        conflicts.checkCategoryName(newCategoryDto);
        CategoryDto answer = mapper.toCategoryDto(
                categoryRepository.save(mapper.toModel(newCategoryDto)).get());
        log.debug("- addCategory. answer: {}", answer.toString());

        return answer;
    }

    @Override
    public long deleteCategory(long catId) {
        log.debug("+ deleteCategory. catId: {}", catId);
        if (categoryRepository.existsById(catId)) {
            conflicts.checkEventsInCategory(catId);
            categoryRepository.deleteById(catId);
            log.debug("- deleteCategory. deleted catId = {}", catId);
            return catId;
        } else {
            throw new ObjectNotFoundException("catId = " + catId + "not found");
        }
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        log.debug("+ updateCategory. catId: {}, categoryDto: {}", catId, categoryDto);

        validations.checkCategoryExistsById(catId);
        conflicts.checkSameCategory(catId, categoryDto);

        categoryDto.setId(catId);
        CategoryDto answer =  mapper.toCategoryDto(categoryRepository.save(mapper.toModel(categoryDto)).get());
        log.debug("- updateCategory. answer: {}", answer);

        return answer;
    }

    @Override
    public Set<EventFullDto> getEvents(List<Long> users, List<String> states,
                                       List<Long> categories, String rangeStart, String rangeEnd,
                                       int from, int size) {
        log.debug("+ getEvents. users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        LocalDateTime start = (rangeStart != null) ? LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime end = (rangeEnd != null) ? LocalDateTime.parse(rangeEnd, formatter) : null;

        validations.checkStartEndTime(start, end);

        Set<EventFullDto> answer = eventRepository.getEventsByAdmin(users, states, categories, start, end,
                        PageRequest.of(from, size))
                            .stream().map(e -> {
                                EventFullDto eventFullDto = mapper.toEventFullDto(e);
                                eventFullDto.setLocation(new Location(e.getLocationLat(), e.getLocationLong()));
                                return eventFullDto;
                            }
        ).collect(Collectors.toSet());
        log.debug("- getEvents. answer: {}", answer);

        return answer;
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("+ updateEvent. eventId = {}. updateEventAdminRequest: {}",
                eventId, updateEventAdminRequest);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("eventId = " + eventId + "not found"));
        event = privateService.prepareEventForUpdate(event, updateEventAdminRequest);

        event = eventRepository.save(event).get();
        log.debug("+ updateEventByUserIdAndEventId. event: {}", event);
        EventFullDto answer = mapper.toEventFullDto(event);
        log.debug("- updateEventByUserIdAndEventId. answer: {}", answer);

        return answer;
    }

    @Override
    public Set<UserDto> getUsers(List<Long> ids, int from, int size) {
        log.debug("+ getUsers. ids: {}. from = {}. size = {}", ids, from, size);
        Comparator<UserDto> byId = Comparator.comparingLong(UserDto::getId);

        Supplier<TreeSet<UserDto>> users = () -> new TreeSet<UserDto>(byId);

        Set<UserDto> answer = userRepository.findByIdIn(ids, PageRequest.of(from, size))
                .stream().map(mapper::toUserDto).collect(Collectors.toCollection(users));
        log.debug("- getUsers. answer: {}", answer);

        return answer;
    }

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        log.debug("+ addUser. newUserRequest: {}", newUserRequest);
        conflicts.checkUserNameExists(newUserRequest);
        UserDto answer =  mapper.toUserDto(userRepository.save(mapper.toModel(newUserRequest)).get());
        log.debug("- addUser. answer: {}", answer);

        return answer;
    }

    @Override
    public long deleteUser(long userId) {
        log.debug("+ deleteUser. userId = {}", userId);
        validations.checkUserExistsById(userId);
        userRepository.deleteById(userId);
        log.debug("- deleteUser. deleted userId = {}", userId);

        return userId;
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        log.debug("+ addCompilation. newCompilationDto: {}", newCompilationDto);
        Set<Event> events = eventRepository.findAllById(newCompilationDto.getEvents()).toSet();
        Compilation compilation = mapper.toModel(newCompilationDto);
        compilation.setEvents(events);

        CompilationDto answer = mapper.toCompilationDto(compilationRepository.save(compilation).get());
        log.debug("- addCompilation. answer: {}", answer);

        return answer;
    }

    @Override
    public long deleteCompilation(long compId) {
        log.debug("+ deleteCompilation. compId = {}", compId);
        validations.checkCompilationExistsById(compId);
        compilationRepository.deleteById(compId);
        log.debug("- deleteCompilation. deletedId = {}", compId);

        return compId;
    }

    @Override
    public CompilationDto updateCompilations(long compId, UpdateCompilationRequest updateCompilationRequest) {
        log.debug("+ updateCompilations. compId = {}, updateCompilationRequest: {}", compId, updateCompilationRequest);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation id = " + compId + " not found"));
        log.debug("+ updateCompilations. compilation: {}", compilation);

        if (updateCompilationRequest.getEvents() != null) {
            if (updateCompilationRequest.getEvents().isEmpty()) {
                compilation.setEvents(Collections.emptySet());
            } else {
                Set<Event> events = new HashSet<>(eventRepository.findAllById(updateCompilationRequest.getEvents()).toList());
                log.debug("+ updateCompilations. events: {}", events);
                compilation.setEvents(events);
            }
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.isPinned()) {
            compilation.setPinned(true);
        }
        if (!updateCompilationRequest.isPinned()) {
            compilation.setPinned(false);
        }
        log.debug("+ updateCompilations. compilation: {}", compilation);
        compilation = compilationRepository.save(compilation).get();
        log.debug("+ updateCompilations. compilation: {}", compilation);

        CompilationDto answer = mapper.toCompilationDto(compilation);
        log.debug("- updateCompilations. answer: {}", answer);

        return answer;
    }
}
