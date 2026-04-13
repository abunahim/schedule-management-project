package com.schedule.schedule_management.repository;

import com.schedule.schedule_management.model.Schedule;
import com.schedule.schedule_management.model.ScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findAll(Pageable pageable);
    Page<Schedule> findByStatus(ScheduleStatus status, Pageable pageable);
    List<Schedule> findByStatus(ScheduleStatus status);
    List<Schedule> findByStatusInAndEndTimeBefore(List<ScheduleStatus> statuses, LocalDateTime time);
}