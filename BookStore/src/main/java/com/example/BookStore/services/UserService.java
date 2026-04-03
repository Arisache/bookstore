package com.example.BookStore.services;

import com.example.BookStore.dtos.CreateUserRequest;
import com.example.BookStore.dtos.UpdateUserRequest;
import com.example.BookStore.dtos.UserDTO;
import com.example.BookStore.entities.User;
import com.example.BookStore.mappers.UserMapper;
import com.example.BookStore.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.toDtoList(users);
    }

    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id : " + id));

        return userMapper.toDto(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDTO createUser(CreateUserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        User saved = userRepository.save(user);

        return userMapper.toDto(saved);
    }

    @Transactional
    public UserDTO updateUser(Long id, UpdateUserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userRequest.getEmail()));

        userMapper.updateEntityFromRequest(userRequest, user);
        User saved = userRepository.save(user);

        return userMapper.toDto(saved);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
}
