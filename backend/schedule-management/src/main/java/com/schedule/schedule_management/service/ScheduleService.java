package com.schedule.schedule_management.service;

import com.schedule.schedule_management.model.Schedule;
import com.schedule.schedule_management.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.schedule.schedule_management.exception.ScheduleNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
    }

    public Schedule updateSchedule(Long id, Schedule updatedSchedule) {
        Schedule existing = getScheduleById(id);
        existing.setTitle(updatedSchedule.getTitle());
        existing.setDescription(updatedSchedule.getDescription());
        existing.setStartTime(updatedSchedule.getStartTime());
        existing.setEndTime(updatedSchedule.getEndTime());
        existing.setStatus(updatedSchedule.getStatus());
        return scheduleRepository.save(existing);
    }

    public void deleteSchedule(Long id) {
        getScheduleById(id);
        scheduleRepository.deleteById(id);
    }
}