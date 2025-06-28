package com.clinicsys.user.controller;

import com.clinicsys.user.model.UserProfile;
import com.clinicsys.user.service.UserProfileService;
import com.clinicsys.common.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "eureka.client.enabled=false",
    "spring.cloud.config.enabled=false",
    "spring.cloud.config.import-check.enabled=false"
})
public class UserProfileControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserProfileService userProfileService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserProfile testProfile;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        // Create test profile
        testProfile = new UserProfile();
        testProfile.setId(1L);
        testProfile.setUserId(1L);
        testProfile.setFirstName("John");
        testProfile.setLastName("Doe");
        testProfile.setEmail("john.doe@example.com");
        testProfile.setPhoneNumber("+1234567890");
        testProfile.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testProfile.setGender(UserProfile.Gender.MALE);
    }

    @Test
    @WithMockUser(username = "1")
    void contextLoads() {
        // Basic context loading test
    }

    @Test
    @WithMockUser(username = "1")
    void getMyProfile_WhenProfileExists_ShouldReturnProfile() throws Exception {
        when(userProfileService.getProfileByUserId(1L)).thenReturn(testProfile);

        mockMvc.perform(get("/api/user/profiles/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(username = "1")
    void getMyProfile_WhenProfileNotExists_ShouldReturnNullData() throws Exception {
        when(userProfileService.getProfileByUserId(1L)).thenThrow(new RuntimeException("Profile not found"));

        mockMvc.perform(get("/api/user/profiles/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile not found - create one"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "1")
    void createMyProfile_ShouldCreateAndReturnProfile() throws Exception {
        when(userProfileService.createProfile(any(UserProfile.class))).thenReturn(testProfile);

        mockMvc.perform(post("/api/user/profiles/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile created successfully"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @WithMockUser(username = "1")
    void updateMyProfile_ShouldUpdateAndReturnProfile() throws Exception {
        when(userProfileService.updateProfileByUserId(eq(1L), any(UserProfile.class))).thenReturn(testProfile);

        mockMvc.perform(put("/api/user/profiles/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @WithMockUser(username = "1")
    void updatePersonalInfo_ShouldUpdatePersonalInfo() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", "Jane");
        updates.put("lastName", "Smith");
        
        when(userProfileService.updatePersonalInfo(eq(1L), any(Map.class))).thenReturn(testProfile);

        mockMvc.perform(patch("/api/user/profiles/me/personal-info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Personal information updated"));
    }

    @Test
    @WithMockUser(username = "1")
    void updateMedicalInfo_ShouldUpdateMedicalInfo() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("medicalConditions", "Hypertension");
        updates.put("allergies", "Penicillin");
        
        when(userProfileService.updateMedicalInfo(eq(1L), any(Map.class))).thenReturn(testProfile);

        mockMvc.perform(patch("/api/user/profiles/me/medical-info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Medical information updated"));
    }

    @Test
    @WithMockUser(username = "1")
    void updatePreferences_ShouldUpdatePreferences() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("preferredAppointmentTime", "MORNING");
        updates.put("languagePreference", "ENGLISH");
        
        when(userProfileService.updatePreferences(eq(1L), any(Map.class))).thenReturn(testProfile);

        mockMvc.perform(patch("/api/user/profiles/me/preferences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Preferences updated"));
    }

    @Test
    @WithMockUser(username = "1")
    void updateEmergencyContact_ShouldUpdateEmergencyContact() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("emergencyContactName", "Jane Doe");
        updates.put("emergencyContactPhone", "+1987654321");
        
        when(userProfileService.updateEmergencyContact(eq(1L), any(Map.class))).thenReturn(testProfile);

        mockMvc.perform(patch("/api/user/profiles/me/emergency-contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Emergency contact updated"));
    }

    @Test
    @WithMockUser(username = "1")
    void getProfileCompletion_ShouldReturnCompletionStatus() throws Exception {
        Map<String, Object> completion = new HashMap<>();
        completion.put("percentage", 75);
        completion.put("completedSections", Arrays.asList("personal", "contact"));
        completion.put("missingSections", Arrays.asList("medical", "emergency"));
        
        when(userProfileService.getProfileCompletion(1L)).thenReturn(completion);

        mockMvc.perform(get("/api/user/profiles/me/completion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile completion status retrieved"))
                .andExpect(jsonPath("$.data.percentage").value(75));
    }

    @Test
    @WithMockUser(username = "1")
    void updateTreatmentProgress_ShouldUpdateTreatmentProgress() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("sessionsCompleted", 5);
        updates.put("totalSessionsPlanned", 10);
        
        when(userProfileService.updateTreatmentProgress(eq(1L), any(Map.class))).thenReturn(testProfile);

        mockMvc.perform(patch("/api/user/profiles/me/treatment-progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Treatment progress updated"));
    }

    @Test
    @WithMockUser(username = "1")
    void updateConsents_ShouldUpdateConsents() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("marketingConsent", true);
        updates.put("photoConsent", false);
        
        when(userProfileService.updateConsents(eq(1L), any(Map.class))).thenReturn(testProfile);

        mockMvc.perform(patch("/api/user/profiles/me/consents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Consents updated"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getProfile_ShouldReturnProfile() throws Exception {
        when(userProfileService.getProfile(1L)).thenReturn(testProfile);

        mockMvc.perform(get("/api/user/profiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllProfiles_ShouldReturnProfilesList() throws Exception {
        List<UserProfile> profiles = Arrays.asList(testProfile);
        when(userProfileService.getAllProfiles(0, 20)).thenReturn(profiles);

        mockMvc.perform(get("/api/user/profiles")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profiles retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateProfile_ShouldUpdateProfile() throws Exception {
        when(userProfileService.updateProfile(eq(1L), any(UserProfile.class))).thenReturn(testProfile);

        mockMvc.perform(put("/api/user/profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile updated successfully"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteProfile_ShouldDeleteProfile() throws Exception {
        mockMvc.perform(delete("/api/user/profiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile deleted successfully"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void searchProfiles_ShouldReturnSearchResults() throws Exception {
        List<UserProfile> profiles = Arrays.asList(testProfile);
        when(userProfileService.searchProfiles("John", 0, 20)).thenReturn(profiles);

        mockMvc.perform(get("/api/user/profiles/search")
                .param("query", "John")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Search results retrieved"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].firstName").value("John"));
    }

    @Test
    void unauthenticatedRequest_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/user/profiles/me"))
                .andExpect(status().isForbidden());
    }
} 