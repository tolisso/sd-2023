package com.github.tolisso.championutstg.service;

import com.github.tolisso.championutstg.model.PassEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final EventStoreService eventStoreService;

    public List<PassEntity> getPassRecords(String userName) {
        return eventStoreService.query.getUserPassRecords(userName)
                .stream()
                .collect(Collectors.toMap(PassEntity::getPassId, Function.identity(),
                        (a, b) -> a.getDueDate().isAfter(b.getDueDate()) ? a : b))
                .values()
                .stream()
                .toList();
    }

    @Transactional
    public void givePass(long passId, String userName, LocalDate dueDate) {
        Optional<String> passIdUserName = eventStoreService.query.getPassRecords(passId).stream()
                .map(PassEntity::getUserName)
                .findAny();
        if (passIdUserName.isEmpty() || passIdUserName.get().equals(userName)) {
            eventStoreService.command.addPass(passId, userName, dueDate);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
