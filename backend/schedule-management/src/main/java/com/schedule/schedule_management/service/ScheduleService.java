package com.schedule.schedule_management.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.schedule.schedule_management.dto.PagedResponseDTO;
import com.schedule.schedule_management.dto.ScheduleMapper;
import com.schedule.schedule_management.dto.ScheduleRequestDTO;
import com.schedule.schedule_management.dto.ScheduleResponseDTO;
import com.schedule.schedule_management.exception.ScheduleNotFoundException;
import com.schedule.schedule_management.model.Schedule;
import com.schedule.schedule_management.model.ScheduleStatus;
import com.schedule.schedule_management.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailService emailService;

    private static final String CACHE_KEY_ALL = "schedules:all";
    private static final String CACHE_KEY_PREFIX = "schedules:";
    private static final long CACHE_TTL = 10;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO dto) {
        Schedule schedule = ScheduleMapper.toEntity(dto);
        ScheduleResponseDTO response = ScheduleMapper.toResponseDTO(scheduleRepository.save(schedule));
        evictCache(response.getId());
        emailService.sendScheduleCreatedEmail(response);
        return response;
    }

    public List<ScheduleResponseDTO> getAllSchedules() {
        Object cached = redisTemplate.opsForValue().get(CACHE_KEY_ALL);
        if (cached != null) {
            log.info("Cache HIT for all schedules");
            return ((List<?>) cached).stream()
                    .map(item -> objectMapper.convertValue(item, ScheduleResponseDTO.class))
                    .collect(Collectors.toList());
        }
        log.info("Cache MISS for all schedules");
        List<ScheduleResponseDTO> result = scheduleRepository.findAll()
                .stream()
                .map(ScheduleMapper::toResponseDTO)
                .collect(Collectors.toList());
        redisTemplate.opsForValue().set(CACHE_KEY_ALL, result, CACHE_TTL, TimeUnit.MINUTES);
        return result;
    }

    public ScheduleResponseDTO getScheduleById(Long id) {
        String key = CACHE_KEY_PREFIX + id;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            log.info("Cache HIT for schedule id: {}", id);
            return objectMapper.convertValue(cached, ScheduleResponseDTO.class);
        }
        log.info("Cache MISS for schedule id: {}", id);
        ScheduleResponseDTO response = scheduleRepository.findById(id)
                .map(ScheduleMapper::toResponseDTO)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
        redisTemplate.opsForValue().set(key, response, CACHE_TTL, TimeUnit.MINUTES);
        return response;
    }

    public ScheduleResponseDTO updateSchedule(Long id, ScheduleRequestDTO dto) {
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setStatus(dto.getStatus());
        ScheduleResponseDTO response = ScheduleMapper.toResponseDTO(scheduleRepository.save(existing));
        evictCache(id);
        emailService.sendScheduleUpdatedEmail(response);
        return response;
    }

    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
        scheduleRepository.deleteById(id);
        evictCache(id);
        emailService.sendScheduleDeletedEmail(id, schedule.getTitle());
    }

    public PagedResponseDTO<ScheduleResponseDTO> getSchedulesPaged(
            int page, int size, String status, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Schedule> result = (status != null && !status.isEmpty())
                ? scheduleRepository.findByStatus(ScheduleStatus.valueOf(status.toUpperCase()), pageable)
                : scheduleRepository.findAll(pageable);

        List<ScheduleResponseDTO> content = result.getContent()
                .stream()
                .map(ScheduleMapper::toResponseDTO)
                .collect(Collectors.toList());

        return PagedResponseDTO.<ScheduleResponseDTO>builder()
                .content(content)
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .last(result.isLast())
                .build();
    }

    private void evictCache(Long id) {
        redisTemplate.delete(CACHE_KEY_PREFIX + id);
        redisTemplate.delete(CACHE_KEY_ALL);
        log.info("Cache evicted for schedule id: {}", id);
    }

    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("SCHEDULED", 0L);
        stats.put("IN_PROGRESS", 0L);
        stats.put("COMPLETED", 0L);
        stats.put("CANCELLED", 0L);

        scheduleRepository.countByStatus().forEach(row -> {
            stats.put(row[0].toString(), (Long) row[1]);
        });

        stats.put("TOTAL", stats.values().stream().mapToLong(Long::longValue).sum());
        return stats;
    }
}