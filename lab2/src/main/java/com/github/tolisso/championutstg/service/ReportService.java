package com.github.tolisso.championutstg.service;

import com.github.tolisso.championutstg.model.PassUsageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final Map<LocalDate, Integer> entersByDay = new ConcurrentHashMap<>();
    private final Map<String, List<PassUsageEntity>> userNameToPassUsages = new ConcurrentHashMap<>();

    protected synchronized void processPassUsage(PassUsageEntity passUsage) {
        userNameToPassUsages.putIfAbsent(passUsage.getUserName(), new ArrayList<>());
        userNameToPassUsages.get(passUsage.getUserName()).add(passUsage);

        if (passUsage.isEnter()) {
            LocalDate day = passUsage.getTimestamp().toLocalDate();
            int oldEntersByDay = entersByDay.getOrDefault(day, 0);
            entersByDay.put(day, oldEntersByDay + 1);
        }
    }

    public long cntEnters(LocalDate day) {
        return entersByDay.getOrDefault(day, 0);
    }

    public double avgHours() {
        List<AvgDurationDto> durations = userNameToPassUsages.values().stream()
                .map(this::durationOfBeingInside)
                .toList();

        double sumDurations = durations.stream()
                .mapToDouble(AvgDurationDto::getDurationSum)
                .sum();

        int numOfDurations = durations.stream()
                .mapToInt(AvgDurationDto::getNumOfDurations)
                .sum();

        return sumDurations / numOfDurations;
    }

    private AvgDurationDto durationOfBeingInside(List<PassUsageEntity> passUsageList) {
        passUsageList.sort(Comparator.comparing(PassUsageEntity::getTimestamp));

        LocalDateTime enterTime = null;
        int numOfDurations = 0;
        double durationSum = 0;

        for (PassUsageEntity passUsage : passUsageList) {
            if (passUsage.isEnter()) {
                enterTime = passUsage.getTimestamp();
            } else if (enterTime != null) {
                long seconds = Duration.between(enterTime, passUsage.getTimestamp()).toSeconds();
                durationSum += ((double) seconds) / 3600;
                numOfDurations++;
                enterTime = null;
            }
        }
        return new AvgDurationDto(numOfDurations, durationSum);
    }

    @AllArgsConstructor
    @Data
    private static class AvgDurationDto {
        private int numOfDurations;
        private double durationSum;
    }
}
