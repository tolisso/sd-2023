package com.github.tolisso.lab4.controller;

import com.github.tolisso.lab4.domain.Order;
import com.github.tolisso.lab4.domain.Visitor;
import com.github.tolisso.lab4.repository.OrderRepository;
import com.github.tolisso.lab4.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final OrderRepository orderRepository;
    private final VisitorRepository visitorRepository;

    @PostMapping("/create-user")
    public void createUser(@RequestParam String visitorName, @RequestParam String currency) {
        visitorRepository
                .save(new Visitor(visitorName, currency))
                .block();
    }

    @PostMapping("/create-order")
    public void createOrder(@RequestParam String visitorName, @RequestParam String orderName, @RequestParam Double price) {
        orderRepository
                .save(new Order(visitorName, orderName, price))
                .block();
    }

    @PostMapping("/get-orders")
    public String getOrders(@RequestParam String visitorName) {
        var visitor = visitorRepository.findByName(visitorName);
        return orderRepository
                .getAllByVisitorName(visitorName)
                .flatMap(order -> visitor.map(
                        vis -> order.getOrderName()
                                + ": "
                                + order.getPrice()
                                + " " + vis.getCurrency()
                ))
                .sort()
                .collect(Collectors.joining(System.lineSeparator()))
                .block();
    }
}
