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
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {
    private static final EwmMapper mapper = EwmMapper.INSTANCE;

    private final CategoryRepository categoryRepository;

    @Override
    public Set<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        return null;
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        return null;
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
    public Set<EventShortDto> getEvents(String text, List<Long> categories, boolean paid, String rangeStart, String rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto getEventById(long id) {
        return null;
    }
}
