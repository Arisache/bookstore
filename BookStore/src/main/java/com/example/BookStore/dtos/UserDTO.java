package com.example.BookStore.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 20, message = "Username must not exceed 20 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^(.+)@(\\\\S+)$", message = "Email must be valid")
    private String email;

    private List<BookDTO> books;
}