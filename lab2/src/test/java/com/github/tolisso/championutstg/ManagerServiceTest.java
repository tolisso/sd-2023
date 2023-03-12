package com.github.tolisso.championutstg;

import com.github.tolisso.championutstg.service.ManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ManagerServiceTest {

    @Autowired
    private ManagerService managerService;

    @Test
    void test() {
        var date1 = LocalDate.of(2021, 1, 1);
        var date2 = LocalDate.of(2022, 1, 1);

        assertTrue(managerService.getPassRecords("Sasha").isEmpty());

        managerService.givePass(1, "Sasha", date1);
        assertEquals(1, managerService.getPassRecords("Sasha").size());
        assertEquals(date1, managerService.getPassRecords("Sasha").get(0).getDueDate());


        managerService.givePass(1, "Sasha", date2);
        assertEquals(1, managerService.getPassRecords("Sasha").size());
        assertEquals(date2, managerService.getPassRecords("Sasha").get(0).getDueDate());

        assertThrows(IllegalArgumentException.class, () -> managerService.givePass(1, "Sasha", date1));
        assertThrows(IllegalArgumentException.class, () -> managerService.givePass(1, "Artem", date2));
    }

}
