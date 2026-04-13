package com.schedule.schedule_management.service;

import com.schedule.schedule_management.model.Schedule;
import com.schedule.schedule_management.model.ScheduleStatus;
import com.schedule.schedule_management.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final ScheduleRepository scheduleRepository;

    // Runs every minute — auto-cancel overdue schedules
    @Scheduled(fixedRate = 60000)
    public void autoCancelOverdueSchedules() {
        List<Schedule> overdue = scheduleRepository.findByStatusInAndEndTimeBefore(
                List.of(ScheduleStatus.SCHEDULED, ScheduleStatus.IN_PROGRESS),
                LocalDateTime.now()
        );

        if (overdue.isEmpty()) {
            log.debug("No overdue schedules found");
            return;
        }

        overdue.forEach(schedule -> {
            schedule.setStatus(ScheduleStatus.CANCELLED);
            scheduleRepository.save(schedule);
            log.info("Auto-cancelled overdue schedule id: {} title: {}", schedule.getId(), schedule.getTitle());
        });

        log.info("Auto-cancelled {} overdue schedule(s)", overdue.size());
    }

    // Runs every day at midnight — log summary
    @Scheduled(cron = "0 0 0 * * *")
    public void dailySummary() {
        long scheduled = scheduleRepository.findByStatus(ScheduleStatus.SCHEDULED).size();
        long inProgress = scheduleRepository.findByStatus(ScheduleStatus.IN_PROGRESS).size();
        long completed = scheduleRepository.findByStatus(ScheduleStatus.COMPLETED).size();
        long cancelled = scheduleRepository.findByStatus(ScheduleStatus.CANCELLED).size();

        log.info("=== Daily Schedule Summary ===");
        log.info("Scheduled:   {}", scheduled);
        log.info("In Progress: {}", inProgress);
        log.info("Completed:   {}", completed);
        log.info("Cancelled:   {}", cancelled);
        log.info("Total:       {}", scheduled + inProgress + completed + cancelled);
        log.info("==============================");
    }
}