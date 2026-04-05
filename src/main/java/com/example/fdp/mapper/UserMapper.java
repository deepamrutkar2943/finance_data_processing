package com.example.fdp.mapper;

import com.example.fdp.dto.response.UserResponse;
import com.example.fdp.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}