package com.schedule.schedule_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.schedule_management.dto.ScheduleRequestDTO;
import com.schedule.schedule_management.model.Role;
import com.schedule.schedule_management.model.ScheduleStatus;
import com.schedule.schedule_management.model.User;
import com.schedule.schedule_management.repository.UserRepository;
import com.schedule.schedule_management.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ScheduleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RedisTemplate<String, Object> redisTemplate;

    @MockBean
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("password123"))
                .role(Role.ROLE_USER)
                .build());
    }

    private String getToken() {
        return "Bearer " + jwtUtil.generateToken("testuser");
    }

    private ScheduleRequestDTO buildRequest(String title) {
        return ScheduleRequestDTO.builder()
                .title(title)
                .description("Test description")
                .startTime(LocalDateTime.of(2026, 4, 15, 10, 0))
                .endTime(LocalDateTime.of(2026, 4, 15, 11, 0))
                .status(ScheduleStatus.SCHEDULED)
                .build();
    }

    @Test
    void createSchedule_shouldReturn201() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        mockMvc.perform(post("/api/schedules")
                        .header("Authorization", getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest("Team Meeting"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Team Meeting"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void getAllSchedules_shouldReturn200() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(null);

        mockMvc.perform(get("/api/schedules")
                        .header("Authorization", getToken()))
                .andExpect(status().isOk());
    }

    @Test
    void getScheduleById_shouldReturn404WhenNotFound() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(null);

        mockMvc.perform(get("/api/schedules/9999")
                        .header("Authorization", getToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Schedule not found with id: 9999"));
    }

    @Test
    void deleteSchedule_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(delete("/api/schedules/9999")
                        .header("Authorization", getToken()))
                .andExpect(status().isNotFound());
    }

    @Test
    void register_shouldReturn200() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newuser\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    void schedules_shouldReturn403WithNoToken() throws Exception {
        mockMvc.perform(get("/api/schedules"))
                .andExpect(status().isForbidden());
    }
}