package com.schedule.schedule_management.controller;

import com.schedule.schedule_management.dto.ScheduleRequestDTO;
import com.schedule.schedule_management.dto.ScheduleResponseDTO;
import com.schedule.schedule_management.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedules", description = "Schedule management endpoints")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(summary = "Create a new schedule")
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@Valid @RequestBody ScheduleRequestDTO dto) {
        return new ResponseEntity<>(scheduleService.createSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all schedules")
    public ResponseEntity<List<ScheduleResponseDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a schedule by ID")
    public ResponseEntity<ScheduleResponseDTO> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a schedule")
    public ResponseEntity<ScheduleResponseDTO> updateSchedule(@PathVariable Long id,
                                                              @Valid @RequestBody ScheduleRequestDTO dto) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a schedule")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}