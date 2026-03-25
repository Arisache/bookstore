package com.example.BookStore.mappers;

import com.example.BookStore.dtos.CreateProfileRequest;
import com.example.BookStore.dtos.ProfileDTO;
import com.example.BookStore.dtos.UpdateProfileRequest;
import com.example.BookStore.entities.Profile;
import com.example.BookStore.entities.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileMapper {

    public ProfileDTO toDto(Profile profile) {
        if (profile == null) {
            return null;
        }

        return ProfileDTO.builder()
                .id(profile.getId())
                .displayName(profile.getDisplayName())
                .bio(profile.getBio())
                .avatarUrl(profile.getAvatarUrl())
                .userId(profile.getUser() != null ? profile.getUser().getId() : null)
                .build();
    }

    public Profile toEntity(ProfileDTO profileDTO) {
        if (profileDTO == null) {
            return null;
        }

        Profile profile = new Profile();
        profile.setId(profileDTO.getId());
        profile.setDisplayName(profileDTO.getDisplayName());
        profile.setBio(profileDTO.getBio());
        profile.setAvatarUrl(profileDTO.getAvatarUrl());
        // User must be set separately in the service layer via userId lookup

        return profile;
    }

    public Profile toEntity(CreateProfileRequest request) {
        if (request == null) {
            return null;
        }

        Profile profile = new Profile();
        profile.setDisplayName(request.getDisplayName());
        profile.setBio(request.getBio());
        profile.setAvatarUrl(request.getAvatarUrl());
        // User is set in the service: profile.setUser(user)

        return profile;
    }

    public void updateEntityFromRequest(UpdateProfileRequest request, Profile profile) {
        if (request == null || profile == null) {
            return;
        }

        profile.setDisplayName(request.getDisplayName());
        profile.setBio(request.getBio());
        profile.setAvatarUrl(request.getAvatarUrl());
    }

    public List<ProfileDTO> toDtoList(List<Profile> profiles) {
        if (profiles == null) {
            return Collections.emptyList();
        }

        return profiles.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}