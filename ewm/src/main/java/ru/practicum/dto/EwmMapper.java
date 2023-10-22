package ru.practicum.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CompilationDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.complination.NewCompilationDto;
import ru.practicum.dto.complination.UpdateCompilationRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.ParticipationRequestDto;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.Category;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;

@Mapper
public interface EwmMapper {
    String dateTimeFormat =  "yyyy-MM-dd HH:mm:ss";
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
    @Mapping(source = "eventDate", target = "eventDate", dateFormat = dateTimeFormat)
    Event toModel(NewEventDto newEventDto);
    @Mapping(ignore = true, target = "location")
    @Mapping(source = "eventDate", target = "eventDate", dateFormat = dateTimeFormat)
    @Mapping(source = "createdOn", target = "createdOn", dateFormat = dateTimeFormat)
    EventFullDto toEventFullDto(Event event);

    @Mapping(source = "eventDate", target = "eventDate", dateFormat = dateTimeFormat)
    EventShortDto toEventShortDto(Event event);

    @Mapping(source = "created", target = "created", dateFormat = dateTimeFormat)
    @Mapping(source = "request.event.id", target = "event")
    @Mapping(source = "request.requester.id", target = "requester")
    ParticipationRequestDto toParticipationRequestDto(Request request);

    @Mapping(ignore = true, target = "events")
    Compilation toModel(NewCompilationDto newCompilationDto);
    CompilationDto toCompilationDto(Compilation compilation);
}