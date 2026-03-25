// ProfileDTO.java
package com.example.BookStore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProfileDTO {
    private Long id;
    private String displayName;
    private String bio;
    private String avatarUrl;
    private Long userId; // just the ID, not the full User object
}