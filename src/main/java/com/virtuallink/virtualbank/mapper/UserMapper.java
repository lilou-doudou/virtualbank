package com.virtuallink.virtualbank.mapper;

import com.virtuallink.virtualbank.dto.UserDto;
import com.virtuallink.virtualbank.models.User;
import com.virtuallink.virtualbank.requests.CreateUserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    /** Convertit une entité User en record UserDto. */
    UserDto toDto(User user);

    /** Convertit une liste d'entités en liste de records UserDto. */
    List<UserDto> toDtoList(List<User> users);

    /** Convertit un CreateUserRequestDto en entité User (sans id, createdAt, updatedAt, accounts). */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    User toEntity(CreateUserRequestDto dto);

    /** Met à jour une entité User depuis un UpdateUserRequestDto. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    void updateEntityFromDto(com.virtuallink.virtualbank.requests.UpdateUserRequestDto dto,
                             @MappingTarget User user);
}

