package com.schedule.schedule_management;

import com.schedule.schedule_management.model.Schedule;
import com.schedule.schedule_management.model.ScheduleStatus;
import com.schedule.schedule_management.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    private Schedule buildSchedule(String title) {
        return Schedule.builder()
                .title(title)
                .description("Test description")
                .startTime(LocalDateTime.of(2026, 4, 15, 10, 0))
                .endTime(LocalDateTime.of(2026, 4, 15, 11, 0))
                .status(ScheduleStatus.SCHEDULED)
                .build();
    }

    @Test
    void save_shouldPersistSchedule() {
        Schedule saved = scheduleRepository.save(buildSchedule("Team Meeting"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Team Meeting");
    }

    @Test
    void findById_shouldReturnSchedule() {
        Schedule saved = scheduleRepository.save(buildSchedule("Team Meeting"));

        Optional<Schedule> found = scheduleRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Team Meeting");
    }

    @Test
    void findAll_shouldReturnAllSchedules() {
        scheduleRepository.save(buildSchedule("Meeting A"));
        scheduleRepository.save(buildSchedule("Meeting B"));

        List<Schedule> all = scheduleRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void deleteById_shouldRemoveSchedule() {
        Schedule saved = scheduleRepository.save(buildSchedule("Team Meeting"));

        scheduleRepository.deleteById(saved.getId());

        assertThat(scheduleRepository.findById(saved.getId())).isEmpty();
    }
}