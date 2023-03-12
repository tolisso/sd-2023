package com.github.tolisso.championutstg.service;

import com.github.tolisso.championutstg.enums.PassUsageType;
import com.github.tolisso.championutstg.model.PassEntity;
import com.github.tolisso.championutstg.model.PassUsageEntity;
import com.github.tolisso.championutstg.repository.PassRepository;
import com.github.tolisso.championutstg.repository.PassUsageRepository;
import com.github.tolisso.championutstg.util.TransactionRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventStoreService {

    private final PassRepository passRepository;
    private final PassUsageRepository passUsageRepository;
    private final TransactionRunner transactionRunner;
    private final ReportService reportService;

    public final Command command = new Command();
    public final Query query = new Query();

    public class Command {

        public void addPass(long passId, String userName, LocalDate dueDate) {
            PassEntity pass = new PassEntity();
            pass.setPassId(passId);
            pass.setUserName(userName);
            pass.setDueDate(dueDate);

            transactionRunner.doInTransaction(() -> {
                LocalDate dueDateOld = passRepository.getAllByPassId(passId).stream()
                        .map(PassEntity::getDueDate)
                        .max(LocalDate::compareTo)
                        .orElse(null);
                if (dueDateOld == null || !dueDate.isBefore(dueDateOld)) {
                    passRepository.save(pass);
                } else {
                    throw new IllegalArgumentException();
                }
            });
        }

        public void addPassUsage(String userName, PassUsageType usageType, LocalDateTime timestamp) {
            PassUsageEntity passUsage = new PassUsageEntity();
            passUsage.setEnter(usageType.boolValue);
            passUsage.setUserName(userName);
            passUsage.setTimestamp(timestamp);

            PassUsageEntity savedPassUsage = passUsageRepository.save(passUsage);
            reportService.processPassUsage(savedPassUsage);
        }
    }

    public class Query {
        public List<PassEntity> getPassRecords(long passId) {
            return passRepository.getAllByPassId(passId);
        }

        public List<PassEntity> getUserPassRecords(String userName) {
            return passRepository.getAllByUserName(userName);
        }

        public List<PassUsageEntity> getAllPassUsageRecords() {
            return passUsageRepository.getAllBy();
        }

        public List<PassUsageEntity> getUserPassUsageRecords(String userName) {
            return passUsageRepository.getAllByUserName(userName);
        }

        public List<PassUsageEntity> getPassUsageByDay(LocalDate day) {
            return passUsageRepository.getAllByTimestampBetween(day.atStartOfDay(), day.atTime(LocalTime.MAX));
        }
    }
}
