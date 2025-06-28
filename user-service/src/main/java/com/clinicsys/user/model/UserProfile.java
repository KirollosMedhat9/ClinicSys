package com.clinicsys.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_profiles")
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true)
    private String email;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    
    // Address Information
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "zip_code")
    private String zipCode;
    
    @Column(name = "country")
    private String country;
    
    // Emergency Contact
    @Column(name = "emergency_contact_name")
    private String emergencyContactName;
    
    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;
    
    @Column(name = "emergency_contact_relationship")
    private String emergencyContactRelationship;
    
    // Medical Information
    @Column(name = "medical_conditions", columnDefinition = "TEXT")
    private String medicalConditions;
    
    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;
    
    @Column(name = "medications", columnDefinition = "TEXT")
    private String medications;
    
    @Column(name = "skin_type")
    private String skinType;
    
    @Column(name = "skin_sensitivity")
    private String skinSensitivity;
    
    @Column(name = "previous_treatments", columnDefinition = "TEXT")
    private String previousTreatments;
    
    @Column(name = "treatment_goals", columnDefinition = "TEXT")
    private String treatmentGoals;
    
    // Treatment Progress
    @Column(name = "current_treatment_plan")
    private String currentTreatmentPlan;
    
    @Column(name = "sessions_completed")
    private Integer sessionsCompleted = 0;
    
    @Column(name = "total_sessions_planned")
    private Integer totalSessionsPlanned = 0;
    
    @Column(name = "last_session_date")
    private LocalDate lastSessionDate;
    
    @Column(name = "next_session_date")
    private LocalDate nextSessionDate;
    
    @Column(name = "treatment_progress_notes", columnDefinition = "TEXT")
    private String treatmentProgressNotes;
    
    // Preferences
    @Column(name = "preferred_clinic_id")
    private Long preferredClinicId;
    
    @Column(name = "preferred_staff_id")
    private Long preferredStaffId;
    
    @Column(name = "preferred_appointment_time")
    private String preferredAppointmentTime; // "MORNING", "AFTERNOON", "EVENING"
    
    @Column(name = "preferred_days")
    private String preferredDays; // "MONDAY,TUESDAY,WEDNESDAY"
    
    @Column(name = "communication_preferences", columnDefinition = "TEXT")
    private String communicationPreferences; // JSON: {"email": true, "sms": false, "push": true}
    
    @Column(name = "language_preference")
    private String languagePreference = "ENGLISH";
    
    @Column(name = "timezone")
    private String timezone = "UTC";
    
    // Marketing and Privacy
    @Column(name = "marketing_consent")
    private Boolean marketingConsent = false;
    
    @Column(name = "data_processing_consent")
    private Boolean dataProcessingConsent = true;
    
    @Column(name = "photo_consent")
    private Boolean photoConsent = false;
    
    // Status and Timestamps
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "profile_completion_percentage")
    private Integer profileCompletionPercentage = 0;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateProfileCompletion();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateProfileCompletion();
    }
    
    private void calculateProfileCompletion() {
        int completed = 0;
        int total = 0;
        
        // Basic info (required)
        total += 3;
        if (firstName != null && !firstName.trim().isEmpty()) completed++;
        if (lastName != null && !lastName.trim().isEmpty()) completed++;
        if (email != null && !email.trim().isEmpty()) completed++;
        
        // Contact info
        total += 1;
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) completed++;
        
        // Personal info
        total += 2;
        if (dateOfBirth != null) completed++;
        if (gender != null) completed++;
        
        // Address
        total += 4;
        if (address != null && !address.trim().isEmpty()) completed++;
        if (city != null && !city.trim().isEmpty()) completed++;
        if (state != null && !state.trim().isEmpty()) completed++;
        if (zipCode != null && !zipCode.trim().isEmpty()) completed++;
        
        // Medical info
        total += 3;
        if (skinType != null && !skinType.trim().isEmpty()) completed++;
        if (skinSensitivity != null && !skinSensitivity.trim().isEmpty()) completed++;
        if (treatmentGoals != null && !treatmentGoals.trim().isEmpty()) completed++;
        
        this.profileCompletionPercentage = total > 0 ? (completed * 100) / total : 0;
    }
    
    // Constructors
    public UserProfile() {}
    
    public UserProfile(Long userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    
    public String getEmergencyContactRelationship() { return emergencyContactRelationship; }
    public void setEmergencyContactRelationship(String emergencyContactRelationship) { this.emergencyContactRelationship = emergencyContactRelationship; }
    
    public String getMedicalConditions() { return medicalConditions; }
    public void setMedicalConditions(String medicalConditions) { this.medicalConditions = medicalConditions; }
    
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    
    public String getMedications() { return medications; }
    public void setMedications(String medications) { this.medications = medications; }
    
    public String getSkinType() { return skinType; }
    public void setSkinType(String skinType) { this.skinType = skinType; }
    
    public String getSkinSensitivity() { return skinSensitivity; }
    public void setSkinSensitivity(String skinSensitivity) { this.skinSensitivity = skinSensitivity; }
    
    public String getPreviousTreatments() { return previousTreatments; }
    public void setPreviousTreatments(String previousTreatments) { this.previousTreatments = previousTreatments; }
    
    public String getTreatmentGoals() { return treatmentGoals; }
    public void setTreatmentGoals(String treatmentGoals) { this.treatmentGoals = treatmentGoals; }
    
    public String getCurrentTreatmentPlan() { return currentTreatmentPlan; }
    public void setCurrentTreatmentPlan(String currentTreatmentPlan) { this.currentTreatmentPlan = currentTreatmentPlan; }
    
    public Integer getSessionsCompleted() { return sessionsCompleted; }
    public void setSessionsCompleted(Integer sessionsCompleted) { this.sessionsCompleted = sessionsCompleted; }
    
    public Integer getTotalSessionsPlanned() { return totalSessionsPlanned; }
    public void setTotalSessionsPlanned(Integer totalSessionsPlanned) { this.totalSessionsPlanned = totalSessionsPlanned; }
    
    public LocalDate getLastSessionDate() { return lastSessionDate; }
    public void setLastSessionDate(LocalDate lastSessionDate) { this.lastSessionDate = lastSessionDate; }
    
    public LocalDate getNextSessionDate() { return nextSessionDate; }
    public void setNextSessionDate(LocalDate nextSessionDate) { this.nextSessionDate = nextSessionDate; }
    
    public String getTreatmentProgressNotes() { return treatmentProgressNotes; }
    public void setTreatmentProgressNotes(String treatmentProgressNotes) { this.treatmentProgressNotes = treatmentProgressNotes; }
    
    public Long getPreferredClinicId() { return preferredClinicId; }
    public void setPreferredClinicId(Long preferredClinicId) { this.preferredClinicId = preferredClinicId; }
    
    public Long getPreferredStaffId() { return preferredStaffId; }
    public void setPreferredStaffId(Long preferredStaffId) { this.preferredStaffId = preferredStaffId; }
    
    public String getPreferredAppointmentTime() { return preferredAppointmentTime; }
    public void setPreferredAppointmentTime(String preferredAppointmentTime) { this.preferredAppointmentTime = preferredAppointmentTime; }
    
    public String getPreferredDays() { return preferredDays; }
    public void setPreferredDays(String preferredDays) { this.preferredDays = preferredDays; }
    
    public String getCommunicationPreferences() { return communicationPreferences; }
    public void setCommunicationPreferences(String communicationPreferences) { this.communicationPreferences = communicationPreferences; }
    
    public String getLanguagePreference() { return languagePreference; }
    public void setLanguagePreference(String languagePreference) { this.languagePreference = languagePreference; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public Boolean getMarketingConsent() { return marketingConsent; }
    public void setMarketingConsent(Boolean marketingConsent) { this.marketingConsent = marketingConsent; }
    
    public Boolean getDataProcessingConsent() { return dataProcessingConsent; }
    public void setDataProcessingConsent(Boolean dataProcessingConsent) { this.dataProcessingConsent = dataProcessingConsent; }
    
    public Boolean getPhotoConsent() { return photoConsent; }
    public void setPhotoConsent(Boolean photoConsent) { this.photoConsent = photoConsent; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Integer getProfileCompletionPercentage() { return profileCompletionPercentage; }
    public void setProfileCompletionPercentage(Integer profileCompletionPercentage) { this.profileCompletionPercentage = profileCompletionPercentage; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public enum Gender {
        MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY
    }
} 