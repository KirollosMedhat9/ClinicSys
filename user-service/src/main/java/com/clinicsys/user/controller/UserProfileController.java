package com.clinicsys.user.controller;

import com.clinicsys.user.model.UserProfile;
import com.clinicsys.user.service.UserProfileService;
import com.clinicsys.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/profiles")
@CrossOrigin(origins = "*")
public class UserProfileController {
    
    @Autowired
    private UserProfileService userProfileService;

    // Get current user's profile
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfile>> getMyProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
        try {
            UserProfile profile = userProfileService.getProfileByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.success("Profile not found - create one", null));
        }
    }

    // Create profile for current user
    @PostMapping("/me")
    public ResponseEntity<ApiResponse<UserProfile>> createMyProfile(@RequestBody UserProfile profile) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        profile.setUserId(userId);
        
        UserProfile created = userProfileService.createProfile(profile);
        return ResponseEntity.ok(ApiResponse.success("Profile created successfully", created));
    }

    // Update current user's profile
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfile>> updateMyProfile(@RequestBody UserProfile profile) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
        UserProfile updated = userProfileService.updateProfileByUserId(userId, profile);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }

    // Update specific sections of profile
    @PatchMapping("/me/personal-info")
    public ResponseEntity<ApiResponse<UserProfile>> updatePersonalInfo(@RequestBody Map<String, Object> updates) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
        UserProfile updated = userProfileService.updatePersonalInfo(userId, updates);
        return ResponseEntity.ok(ApiResponse.success("Personal information updated", updated));
    }

    @PatchMapping("/me/medical-info")
    public ResponseEntity<ApiResponse<UserProfile>> updateMedicalInfo(@RequestBody Map<String, Object> updates) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
        UserProfile updated = userProfileService.updateMedicalInfo(userId, updates);
        return ResponseEntity.ok(ApiResponse.success("Medical information updated", updated));
    }

    @PatchMapping("/me/preferences")
    public ResponseEntity<ApiResponse<UserProfile>> updatePreferences(@RequestBody Map<String, Object> updates) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
        UserProfile updated = userProfileService.updatePreferences(userId, updates);
        return ResponseEntity.ok(ApiResponse.success("Preferences updated", updated));
    }

    @PatchMapping("/me/emergency-contact")
    public ResponseEntity<ApiResponse<UserProfile>> updateEmergencyContact(@RequestBody Map<String, Object> updates) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
        UserProfile updated = userProfileService.updateEmergencyContact(userId, updates);
        return ResponseEntity.ok(ApiResponse.success("Emergency contact updated", updated));
    }

    // Get profile completion status
    @GetMapping("/me/completion")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProfileCompletion() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
        Map<String, Object> completion = userProfileService.getProfileCompletion(userId);
        return ResponseEntity.ok(ApiResponse.success("Profile completion status retrieved", completion));
    }

    // Update treatment progress
    @PatchMapping("/me/treatment-progress")
    public ResponseEntity<ApiResponse<UserProfile>> updateTreatmentProgress(@RequestBody Map<String, Object> updates) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
        UserProfile updated = userProfileService.updateTreatmentProgress(userId, updates);
        return ResponseEntity.ok(ApiResponse.success("Treatment progress updated", updated));
    }

    // Update consents
    @PatchMapping("/me/consents")
    public ResponseEntity<ApiResponse<UserProfile>> updateConsents(@RequestBody Map<String, Object> updates) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
        UserProfile updated = userProfileService.updateConsents(userId, updates);
        return ResponseEntity.ok(ApiResponse.success("Consents updated", updated));
    }

    // Admin endpoints
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfile>> getProfile(@PathVariable Long id) {
        UserProfile profile = userProfileService.getProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfile>>> getAllProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<UserProfile> profiles = userProfileService.getAllProfiles(page, size);
        return ResponseEntity.ok(ApiResponse.success("Profiles retrieved successfully", profiles));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfile>> updateProfile(@PathVariable Long id, @RequestBody UserProfile profile) {
        UserProfile updated = userProfileService.updateProfile(id, profile);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable Long id) {
        userProfileService.deleteProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Profile deleted successfully", null));
    }

    // Search profiles (admin only)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchProfiles(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<UserProfile> profiles = userProfileService.searchProfiles(query, page, size);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved", profiles));
    }
} 