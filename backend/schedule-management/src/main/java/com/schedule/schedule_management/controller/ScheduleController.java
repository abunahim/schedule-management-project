package com.schedule.schedule_management.controller;

import com.schedule.schedule_management.dto.ScheduleRequestDTO;
import com.schedule.schedule_management.dto.ScheduleResponseDTO;
import com.schedule.schedule_management.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@Valid @RequestBody ScheduleRequestDTO dto) {
        return new ResponseEntity<>(scheduleService.createSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> updateSchedule(@PathVariable Long id,
                                                              @Valid @RequestBody ScheduleRequestDTO dto) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}