package com.schedule.schedule_management.service;

import com.schedule.schedule_management.dto.ScheduleMapper;
import com.schedule.schedule_management.dto.ScheduleRequestDTO;
import com.schedule.schedule_management.dto.ScheduleResponseDTO;
import com.schedule.schedule_management.exception.ScheduleNotFoundException;
import com.schedule.schedule_management.model.Schedule;
import com.schedule.schedule_management.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO dto) {
        Schedule schedule = ScheduleMapper.toEntity(dto);
        return ScheduleMapper.toResponseDTO(scheduleRepository.save(schedule));
    }

    public List<ScheduleResponseDTO> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(ScheduleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ScheduleResponseDTO getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .map(ScheduleMapper::toResponseDTO)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
    }

    public ScheduleResponseDTO updateSchedule(Long id, ScheduleRequestDTO dto) {
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setStatus(dto.getStatus());
        return ScheduleMapper.toResponseDTO(scheduleRepository.save(existing));
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
        scheduleRepository.deleteById(id);
    }
}