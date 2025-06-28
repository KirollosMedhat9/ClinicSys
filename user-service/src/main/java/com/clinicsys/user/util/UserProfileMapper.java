package com.clinicsys.user.util;

import com.clinicsys.user.model.UserProfile;
import com.clinicsys.user.dto.UserProfileDto;

public class UserProfileMapper {
    public static UserProfileDto toDto(UserProfile profile) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUserId());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setEmail(profile.getEmail());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setDateOfBirth(profile.getDateOfBirth());
        dto.setGender(profile.getGender());
        // ... map other fields as needed
        return dto;
    }
    // Add fromDto if needed
} 