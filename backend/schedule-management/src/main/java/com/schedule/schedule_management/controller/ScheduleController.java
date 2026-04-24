package com.schedule.schedule_management.controller;

import com.schedule.schedule_management.dto.PagedResponseDTO;
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
import java.util.Map;

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

    @GetMapping("/stats")
    @Operation(summary = "Get schedule stats by status")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(scheduleService.getStats());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a schedule")
    public ResponseEntity<ScheduleResponseDTO> updateSchedule(@PathVariable Long id,
                                                              @Valid @RequestBody ScheduleRequestDTO dto) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, dto));
    }

    @GetMapping("/paged")
    @Operation(summary = "Get schedules with pagination and filtering")
    public ResponseEntity<PagedResponseDTO<ScheduleResponseDTO>> getSchedulesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "startTime") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(scheduleService.getSchedulesPaged(page, size, status, sortBy, sortDir));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a schedule")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}