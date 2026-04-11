package com.schedule.schedule_management.dto;

import com.schedule.schedule_management.model.Schedule;

public class ScheduleMapper {

    public static Schedule toEntity(ScheduleRequestDTO dto) {
        return Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(dto.getStatus())
                .build();
    }

    public static ScheduleResponseDTO toResponseDTO(Schedule schedule) {
        return ScheduleResponseDTO.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .status(schedule.getStatus())
                .createdAt(schedule.getCreatedAt())
                .build();
    }
}