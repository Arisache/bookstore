package com.example.BookStore.mappers;

import com.example.BookStore.dtos.UserDTO;
import com.example.BookStore.dtos.CreateUserRequest;
import com.example.BookStore.dtos.UpdateUserRequest;
import com.example.BookStore.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BookMapper bookMapper;

    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                // Map nested profiles list — using profileMapper for consistency
                .books(bookMapper.toDtoList(user.getBooks()))
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        // Note: we don't map profiles here to avoid circular persistence issues

        return user;
    }

    public User toEntity(CreateUserRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        return user;
    }

    public void updateEntityFromRequest(UpdateUserRequest request, User user) {
        if (request == null || user == null) {
            return;
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
    }

    public List<UserDTO> toDtoList(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}