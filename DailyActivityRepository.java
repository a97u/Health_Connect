package com.healthconnect.repository;

import com.healthconnect.entity.DailyActivity;
import com.healthconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyActivityRepository extends JpaRepository<DailyActivity, Long> {
    List<DailyActivity> findByUser(User user);
    List<DailyActivity> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}