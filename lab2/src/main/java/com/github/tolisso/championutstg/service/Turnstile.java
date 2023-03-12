package com.github.tolisso.championutstg.service;

import com.github.tolisso.championutstg.enums.PassUsageType;
import com.github.tolisso.championutstg.model.PassEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class Turnstile {
    private final EventStoreService eventStoreService;

    public boolean passThroughTurnstile(String userName, PassUsageType passUsageType, LocalDateTime timestamp) {
        if (passUsageType == PassUsageType.EXIT) {
            eventStoreService.command.addPassUsage(userName, PassUsageType.EXIT, timestamp);
            return true;
        } else {
            LocalDate passWorkingDueDate = eventStoreService.query.getUserPassRecords(userName).stream()
                    .map(PassEntity::getDueDate)
                    .max(LocalDate::compareTo)
                    .orElse(LocalDate.MIN);

            if (!timestamp.toLocalDate().isAfter(passWorkingDueDate)) {
                eventStoreService.command.addPassUsage(userName, PassUsageType.ENTER, timestamp);
                return true;
            } else {
                return false;
            }
        }
    }
}
