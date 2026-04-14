package com.schedule.schedule_management.service;

import com.schedule.schedule_management.dto.ScheduleResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String mailFrom;

    @Value("${app.mail.to}")
    private String mailTo;

    @Async
    public void sendScheduleCreatedEmail(ScheduleResponseDTO schedule) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(mailTo);
            message.setSubject("✅ Schedule Created: " + schedule.getTitle());
            message.setText(buildEmailBody("created", schedule));
            mailSender.send(message);
            log.info("Schedule created email sent for id: {}", schedule.getId());
        } catch (Exception e) {
            log.error("Failed to send schedule created email: {}", e.getMessage());
        }
    }

    @Async
    public void sendScheduleUpdatedEmail(ScheduleResponseDTO schedule) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(mailTo);
            message.setSubject("✏️ Schedule Updated: " + schedule.getTitle());
            message.setText(buildEmailBody("updated", schedule));
            mailSender.send(message);
            log.info("Schedule updated email sent for id: {}", schedule.getId());
        } catch (Exception e) {
            log.error("Failed to send schedule updated email: {}", e.getMessage());
        }
    }

    @Async
    public void sendScheduleDeletedEmail(Long id, String title) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(mailTo);
            message.setSubject("🗑️ Schedule Deleted: " + title);
            message.setText("Schedule '" + title + "' (id: " + id + ") has been deleted.");
            mailSender.send(message);
            log.info("Schedule deleted email sent for id: {}", id);
        } catch (Exception e) {
            log.error("Failed to send schedule deleted email: {}", e.getMessage());
        }
    }

    private String buildEmailBody(String action, ScheduleResponseDTO schedule) {
        return String.format("""
                Schedule %s successfully.
                
                Title:       %s
                Description: %s
                Start Time:  %s
                End Time:    %s
                Status:      %s
                Created At:  %s
                """,
                action,
                schedule.getTitle(),
                schedule.getDescription(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getStatus(),
                schedule.getCreatedAt()
        );
    }
}