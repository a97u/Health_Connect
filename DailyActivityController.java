package com.healthconnect.controller;

import com.healthconnect.dto.DailyActivityDTO;
import com.healthconnect.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class DailyActivityController {

    @Autowired
    private DailyActivityService dailyActivityService;

    @PostMapping
    public ResponseEntity<DailyActivityDTO> createActivity(@RequestBody DailyActivityDTO dailyActivityDTO, Authentication authentication) {
        DailyActivityDTO createdActivity = dailyActivityService.createActivity(dailyActivityDTO, authentication.getName());
        return ResponseEntity.ok(createdActivity);
    }

    @GetMapping
    public ResponseEntity<List<DailyActivityDTO>> getActivities(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyActivityDTO> activities = dailyActivityService.getActivitiesByUser(authentication.getName(), startDate, endDate);
        return ResponseEntity.ok(activities);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyActivityDTO> updateActivity(@PathVariable Long id, @RequestBody DailyActivityDTO dailyActivityDTO, Authentication authentication) {
        DailyActivityDTO updatedActivity = dailyActivityService.updateActivity(id, dailyActivityDTO, authentication.getName());
        return ResponseEntity.ok(updatedActivity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id, Authentication authentication) {
        dailyActivityService.deleteActivity(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}