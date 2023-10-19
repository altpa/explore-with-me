package ru.practicum.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;

@Mapper
public interface EwmMapper {
    EwmMapper INSTANCE = Mappers.getMapper(EwmMapper.class);

    Category toModel(CategoryDto categoryDto);
    Category toModel(NewCategoryDto newCategoryDto);
    CategoryDto toCategoryDto(Category category);

    User toModel(NewUserRequest newUserRequest);
    UserDto toUserDto(User user);
    UserShortDto toUserShortDto(User user);

    @Mapping(ignore = true, target = "category")
    @Mapping(ignore = true, target = "initiator")
    @Mapping(ignore = true, target = "locationLat")
    @Mapping(ignore = true, target = "locationLong")
    Event toModel(NewEventDto newEventDto);
    @Mapping(ignore = true, target = "location")
    EventFullDto toDto(Event event);
}