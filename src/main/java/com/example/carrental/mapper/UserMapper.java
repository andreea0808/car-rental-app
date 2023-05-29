package com.example.carrental.mapper;

import com.example.carrental.dto.UserDto;
import com.example.carrental.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
