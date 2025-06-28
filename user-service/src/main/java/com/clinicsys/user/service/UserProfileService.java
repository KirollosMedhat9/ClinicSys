package com.clinicsys.user.service;

import com.clinicsys.user.model.UserProfile;
import com.clinicsys.user.repository.UserProfileRepository;
import com.clinicsys.user.exception.UserProfileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile createProfile(UserProfile profile) {
        return userProfileRepository.save(profile);
    }

    public UserProfile getProfile(Long id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found with id: " + id));
    }

    public UserProfile getProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found for user: " + userId));
    }

    public List<UserProfile> getAllProfiles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userProfileRepository.findAll(pageable).getContent();
    }

    public UserProfile updateProfile(Long id, UserProfile updated) {
        UserProfile profile = getProfile(id);
        updateProfileFields(profile, updated);
        return userProfileRepository.save(profile);
    }

    public UserProfile updateProfileByUserId(Long userId, UserProfile updated) {
        UserProfile profile = getProfileByUserId(userId);
        updateProfileFields(profile, updated);
        return userProfileRepository.save(profile);
    }

    public UserProfile updatePersonalInfo(Long userId, Map<String, Object> updates) {
        UserProfile profile = getProfileByUserId(userId);
        
        if (updates.containsKey("firstName")) profile.setFirstName((String) updates.get("firstName"));
        if (updates.containsKey("lastName")) profile.setLastName((String) updates.get("lastName"));
        if (updates.containsKey("email")) profile.setEmail((String) updates.get("email"));
        if (updates.containsKey("phoneNumber")) profile.setPhoneNumber((String) updates.get("phoneNumber"));
        if (updates.containsKey("dateOfBirth")) profile.setDateOfBirth(LocalDate.parse((String) updates.get("dateOfBirth")));
        if (updates.containsKey("gender")) profile.setGender(UserProfile.Gender.valueOf((String) updates.get("gender")));
        if (updates.containsKey("address")) profile.setAddress((String) updates.get("address"));
        if (updates.containsKey("city")) profile.setCity((String) updates.get("city"));
        if (updates.containsKey("state")) profile.setState((String) updates.get("state"));
        if (updates.containsKey("zipCode")) profile.setZipCode((String) updates.get("zipCode"));
        if (updates.containsKey("country")) profile.setCountry((String) updates.get("country"));
        
        return userProfileRepository.save(profile);
    }

    public UserProfile updateMedicalInfo(Long userId, Map<String, Object> updates) {
        UserProfile profile = getProfileByUserId(userId);
        
        if (updates.containsKey("medicalConditions")) profile.setMedicalConditions((String) updates.get("medicalConditions"));
        if (updates.containsKey("allergies")) profile.setAllergies((String) updates.get("allergies"));
        if (updates.containsKey("medications")) profile.setMedications((String) updates.get("medications"));
        if (updates.containsKey("skinType")) profile.setSkinType((String) updates.get("skinType"));
        if (updates.containsKey("skinSensitivity")) profile.setSkinSensitivity((String) updates.get("skinSensitivity"));
        if (updates.containsKey("previousTreatments")) profile.setPreviousTreatments((String) updates.get("previousTreatments"));
        if (updates.containsKey("treatmentGoals")) profile.setTreatmentGoals((String) updates.get("treatmentGoals"));
        
        return userProfileRepository.save(profile);
    }

    public UserProfile updatePreferences(Long userId, Map<String, Object> updates) {
        UserProfile profile = getProfileByUserId(userId);
        
        if (updates.containsKey("preferredClinicId")) profile.setPreferredClinicId(Long.valueOf((String) updates.get("preferredClinicId")));
        if (updates.containsKey("preferredStaffId")) profile.setPreferredStaffId(Long.valueOf((String) updates.get("preferredStaffId")));
        if (updates.containsKey("preferredAppointmentTime")) profile.setPreferredAppointmentTime((String) updates.get("preferredAppointmentTime"));
        if (updates.containsKey("preferredDays")) profile.setPreferredDays((String) updates.get("preferredDays"));
        if (updates.containsKey("communicationPreferences")) profile.setCommunicationPreferences((String) updates.get("communicationPreferences"));
        if (updates.containsKey("languagePreference")) profile.setLanguagePreference((String) updates.get("languagePreference"));
        if (updates.containsKey("timezone")) profile.setTimezone((String) updates.get("timezone"));
        
        return userProfileRepository.save(profile);
    }

    public UserProfile updateEmergencyContact(Long userId, Map<String, Object> updates) {
        UserProfile profile = getProfileByUserId(userId);
        
        if (updates.containsKey("emergencyContactName")) profile.setEmergencyContactName((String) updates.get("emergencyContactName"));
        if (updates.containsKey("emergencyContactPhone")) profile.setEmergencyContactPhone((String) updates.get("emergencyContactPhone"));
        if (updates.containsKey("emergencyContactRelationship")) profile.setEmergencyContactRelationship((String) updates.get("emergencyContactRelationship"));
        
        return userProfileRepository.save(profile);
    }

    public UserProfile updateTreatmentProgress(Long userId, Map<String, Object> updates) {
        UserProfile profile = getProfileByUserId(userId);
        
        if (updates.containsKey("currentTreatmentPlan")) profile.setCurrentTreatmentPlan((String) updates.get("currentTreatmentPlan"));
        if (updates.containsKey("sessionsCompleted")) profile.setSessionsCompleted((Integer) updates.get("sessionsCompleted"));
        if (updates.containsKey("totalSessionsPlanned")) profile.setTotalSessionsPlanned((Integer) updates.get("totalSessionsPlanned"));
        if (updates.containsKey("lastSessionDate")) profile.setLastSessionDate(LocalDate.parse((String) updates.get("lastSessionDate")));
        if (updates.containsKey("nextSessionDate")) profile.setNextSessionDate(LocalDate.parse((String) updates.get("nextSessionDate")));
        if (updates.containsKey("treatmentProgressNotes")) profile.setTreatmentProgressNotes((String) updates.get("treatmentProgressNotes"));
        
        return userProfileRepository.save(profile);
    }

    public UserProfile updateConsents(Long userId, Map<String, Object> updates) {
        UserProfile profile = getProfileByUserId(userId);
        
        if (updates.containsKey("marketingConsent")) profile.setMarketingConsent((Boolean) updates.get("marketingConsent"));
        if (updates.containsKey("dataProcessingConsent")) profile.setDataProcessingConsent((Boolean) updates.get("dataProcessingConsent"));
        if (updates.containsKey("photoConsent")) profile.setPhotoConsent((Boolean) updates.get("photoConsent"));
        
        return userProfileRepository.save(profile);
    }

    public Map<String, Object> getProfileCompletion(Long userId) {
        UserProfile profile = getProfileByUserId(userId);
        
        Map<String, Object> completion = new HashMap<>();
        completion.put("percentage", profile.getProfileCompletionPercentage());
        completion.put("completedSections", getCompletedSections(profile));
        completion.put("missingSections", getMissingSections(profile));
        
        return completion;
    }

    public List<UserProfile> searchProfiles(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // This would need a custom repository method for search
        // For now, return all profiles
        return userProfileRepository.findAll(pageable).getContent();
    }

    public void deleteProfile(Long id) {
        userProfileRepository.deleteById(id);
    }

    private void updateProfileFields(UserProfile profile, UserProfile updated) {
        if (updated.getFirstName() != null) profile.setFirstName(updated.getFirstName());
        if (updated.getLastName() != null) profile.setLastName(updated.getLastName());
        if (updated.getEmail() != null) profile.setEmail(updated.getEmail());
        if (updated.getPhoneNumber() != null) profile.setPhoneNumber(updated.getPhoneNumber());
        if (updated.getDateOfBirth() != null) profile.setDateOfBirth(updated.getDateOfBirth());
        if (updated.getGender() != null) profile.setGender(updated.getGender());
        if (updated.getAddress() != null) profile.setAddress(updated.getAddress());
        if (updated.getCity() != null) profile.setCity(updated.getCity());
        if (updated.getState() != null) profile.setState(updated.getState());
        if (updated.getZipCode() != null) profile.setZipCode(updated.getZipCode());
        if (updated.getCountry() != null) profile.setCountry(updated.getCountry());
        // Add other fields as needed
    }

    private List<String> getCompletedSections(UserProfile profile) {
        // Implementation to return list of completed sections
        return List.of("Basic Information"); // Placeholder
    }

    private List<String> getMissingSections(UserProfile profile) {
        // Implementation to return list of missing sections
        return List.of("Medical Information", "Emergency Contact"); // Placeholder
    }
} 