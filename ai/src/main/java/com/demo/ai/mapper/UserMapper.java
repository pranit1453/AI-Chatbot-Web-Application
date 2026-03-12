package com.demo.ai.mapper;

import com.demo.ai.dto.request.CreateUserRequest;
import com.demo.ai.dto.request.UpdateUserRequest;
import com.demo.ai.dto.response.UserResponse;
import com.demo.ai.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(CreateUserRequest request);

    UserResponse toResponse(User user);

    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}
