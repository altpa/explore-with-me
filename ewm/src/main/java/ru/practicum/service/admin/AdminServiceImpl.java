package ru.practicum.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EwmMapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.complination.NewCompilationDto;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.complination.UpdateCompilationRequest;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private static final EwmMapper mapper = EwmMapper.INSTANCE;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        log.debug("+ addCategory. newCategoryDto: {}", newCategoryDto.toString());
        if (!categoryRepository.existsByName(newCategoryDto.getName())) {
            CategoryDto answer = mapper.toCategoryDto(
                    categoryRepository.save(mapper.toModel(newCategoryDto)).get());
            log.debug("- addCategory. answer: {}", answer.toString());
            return answer;
        } else {
            throw new BadRequestException("The name already exist");
        }
    }

    @Override
    public long deleteCategory(long catId) {
        log.debug("+ deleteCategory. catId: {}", catId);
        if (categoryRepository.existsById(catId)) {
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
        if (categoryRepository.existsById(catId)) {
            categoryDto.setId(catId);
            CategoryDto answer =  mapper.toCategoryDto(categoryRepository.save(mapper.toModel(categoryDto)).get());
            log.debug("- updateCategory. answer: {}", answer);
            return answer;
        } else {
            throw new ObjectNotFoundException("catId = " + catId + "not found");
        }
    }

    @Override
    public Set<EventFullDto> getEvents(List<Integer> users, List<String> states,
                                       List<Long> categories, String rangeStart, String rangeEnd,
                                       int from, int size) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        return null;
    }

    @Override
    public Set<UserDto> getUsers(List<Long> ids, int from, int size) {
        log.debug("+ getUsers. ids: {}. from = {}. size = {}", ids, from, size);
        Set<UserDto> answer = userRepository.findByIdIn(ids, PageRequest.of(from, size))
                .stream().map(mapper::toUserDto).collect(Collectors.toSet());
        log.debug("- getUsers. answer: {}", answer);
        return answer;
    }

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        log.debug("+ addUser. newUserRequest: {}", newUserRequest);
        UserDto answer =  mapper.toUserDto(userRepository.save(mapper.toModel(newUserRequest)).get());
        log.debug("- addUser. answer: {}", answer);
        return answer;
    }

    @Override
    public long deleteUser(long userId) {
        log.debug("+ deleteUser. userId = {}", userId);
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            log.debug("- deleteUser. deleted userId = {}", userId);
            return userId;
        } else {
            throw new ObjectNotFoundException("no user with userId = " + userId);
        }
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        return null;
    }

    @Override
    public void deleteCompilation(long compId) {

    }

    @Override
    public EventFullDto updateEvent(long compId, UpdateCompilationRequest updateCompilationRequest) {
        return null;
    }
}
