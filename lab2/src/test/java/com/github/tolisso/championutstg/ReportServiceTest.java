package com.github.tolisso.championutstg;

import com.github.tolisso.championutstg.enums.PassUsageType;
import com.github.tolisso.championutstg.service.ManagerService;
import com.github.tolisso.championutstg.service.ReportService;
import com.github.tolisso.championutstg.service.Turnstile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private Turnstile turnstile;

    @Autowired
    private ManagerService managerService;

    @Test
    void test() {

        managerService.givePass(1, "Sasha", LocalDate.of(2023, 1, 1));

        LocalDate day = LocalDate.of(2022, 1, 1);
        turnstile.passThroughTurnstile("Sasha", PassUsageType.ENTER, day.atTime(LocalTime.of(1, 0)));
        turnstile.passThroughTurnstile("Sasha", PassUsageType.EXIT, day.atTime(LocalTime.of(2, 0)));
        turnstile.passThroughTurnstile("Sasha", PassUsageType.ENTER, day.atTime(LocalTime.of(3, 0)));
        turnstile.passThroughTurnstile("Sasha", PassUsageType.EXIT, day.atTime(LocalTime.of(3, 30)));

        assertEquals(0.75, reportService.avgHours());
        assertEquals(2, reportService.cntEnters(day));
    }

}
