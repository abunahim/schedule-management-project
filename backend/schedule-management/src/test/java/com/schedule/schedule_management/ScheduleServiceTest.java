package com.schedule.schedule_management;

import com.schedule.schedule_management.dto.ScheduleRequestDTO;
import com.schedule.schedule_management.dto.ScheduleResponseDTO;
import com.schedule.schedule_management.exception.ScheduleNotFoundException;
import com.schedule.schedule_management.model.Schedule;
import com.schedule.schedule_management.model.ScheduleStatus;
import com.schedule.schedule_management.repository.ScheduleRepository;
import com.schedule.schedule_management.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private ScheduleService scheduleService;

    private Schedule schedule;
    private ScheduleRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        schedule = Schedule.builder()
                .id(1L)
                .title("Team Meeting")
                .description("Weekly sync")
                .startTime(LocalDateTime.of(2026, 4, 15, 10, 0))
                .endTime(LocalDateTime.of(2026, 4, 15, 11, 0))
                .status(ScheduleStatus.SCHEDULED)
                .createdAt(LocalDateTime.now())
                .build();

        requestDTO = ScheduleRequestDTO.builder()
                .title("Team Meeting")
                .description("Weekly sync")
                .startTime(LocalDateTime.of(2026, 4, 15, 10, 0))
                .endTime(LocalDateTime.of(2026, 4, 15, 11, 0))
                .status(ScheduleStatus.SCHEDULED)
                .build();

        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void createSchedule_shouldReturnResponseDTO() {
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        ScheduleResponseDTO response = scheduleService.createSchedule(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Team Meeting");
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    void getAllSchedules_shouldReturnList() {
        when(valueOperations.get(any())).thenReturn(null);
        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));

        List<ScheduleResponseDTO> result = scheduleService.getAllSchedules();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Team Meeting");
    }

    @Test
    void getScheduleById_shouldReturnResponseDTO() {
        when(valueOperations.get(any())).thenReturn(null);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        ScheduleResponseDTO result = scheduleService.getScheduleById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getScheduleById_shouldThrowWhenNotFound() {
        when(valueOperations.get(any())).thenReturn(null);
        when(scheduleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.getScheduleById(99L))
                .isInstanceOf(ScheduleNotFoundException.class);
    }

    @Test
    void deleteSchedule_shouldCallDeleteById() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        scheduleService.deleteSchedule(1L);

        verify(scheduleRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteSchedule_shouldThrowWhenNotFound() {
        when(scheduleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.deleteSchedule(99L))
                .isInstanceOf(ScheduleNotFoundException.class);
    }
}