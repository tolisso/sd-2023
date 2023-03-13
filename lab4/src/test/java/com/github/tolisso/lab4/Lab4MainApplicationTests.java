package com.github.tolisso.lab4;

import com.github.tolisso.lab4.controller.MainController;
import com.github.tolisso.lab4.repository.OrderRepository;
import com.github.tolisso.lab4.repository.VisitorRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
class Lab4MainApplicationTests {

    @Autowired
    MainController mainController;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    VisitorRepository visitorRepository;

    @BeforeEach
    public void before() {
        orderRepository.deleteAll().block();
        visitorRepository.deleteAll().block();
    }

    @Test
    void contextLoads() {
        mainController.createUser("Ann", "Rub");
        mainController.createOrder("Ann", "Strapon", 1999.0);
        mainController.createOrder("Ann", "Collar", 599.0);
        assertEquals( "Collar: 599.0 Rub" + System.lineSeparator() + "Strapon: 1999.0 Rub",
                mainController.getOrders("Ann"));
    }

}
