package com.healthconnect.service;

import com.healthconnect.dto.DailyActivityDTO;
import com.healthconnect.entity.DailyActivity;
import com.healthconnect.entity.User;
import com.healthconnect.repository.DailyActivityRepository;
import com.healthconnect.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyActivityService {
    private static final Logger logger = LoggerFactory.getLogger(DailyActivityService.class);

    @Autowired
    private DailyActivityRepository dailyActivityRepository;

    @Autowired
    private UserRepository userRepository;

    public DailyActivityDTO createActivity(DailyActivityDTO dto, String username) {
        logger.info("Creating activity for user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        DailyActivity activity = new DailyActivity();
        activity.setUser(user);
        activity.setDate(dto.getDate());
        activity.setSteps(dto.getSteps());
        activity.setCaloriesBurned(dto.getCaloriesBurned());
        activity.setWorkoutType(dto.getWorkoutType());
        activity.setDurationMinutes(dto.getDurationMinutes());
        DailyActivity savedActivity = dailyActivityRepository.save(activity);
        return convertToDTO(savedActivity);
    }

    public List<DailyActivityDTO> getActivitiesByUser(String username, LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching activities for user: {}", username);
        if (username == null) {
            logger.error("Username is null");
            throw new IllegalArgumentException("Username cannot be null");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<DailyActivity> activities = (startDate != null && endDate != null)
                ? dailyActivityRepository.findByUserAndDateBetween(user, startDate, endDate)
                : dailyActivityRepository.findByUser(user);
        return activities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public DailyActivityDTO updateActivity(Long id, DailyActivityDTO dto, String username) {
        logger.info("Updating activity {} for user: {}", id, username);
        DailyActivity activity = dailyActivityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        if (!activity.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to activity");
        }
        activity.setDate(dto.getDate());
        activity.setSteps(dto.getSteps());
        activity.setCaloriesBurned(dto.getCaloriesBurned());
        activity.setWorkoutType(dto.getWorkoutType());
        activity.setDurationMinutes(dto.getDurationMinutes());
        DailyActivity updatedActivity = dailyActivityRepository.save(activity);
        return convertToDTO(updatedActivity);
    }

    public void deleteActivity(Long id, String username) {
        logger.info("Deleting activity {} for user: {}", id, username);
        DailyActivity activity = dailyActivityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        if (!activity.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to activity");
        }
        dailyActivityRepository.delete(activity);
    }

    private DailyActivityDTO convertToDTO(DailyActivity activity) {
        DailyActivityDTO dto = new DailyActivityDTO();
        dto.setId(activity.getId());
        dto.setUserId(activity.getUser().getId());
        dto.setDate(activity.getDate());
        dto.setSteps(activity.getSteps());
        dto.setCaloriesBurned(activity.getCaloriesBurned());
        dto.setWorkoutType(activity.getWorkoutType());
        dto.setDurationMinutes(activity.getDurationMinutes());
        return dto;
    }
}