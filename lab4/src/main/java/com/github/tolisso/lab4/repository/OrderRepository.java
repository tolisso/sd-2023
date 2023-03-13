package com.github.tolisso.lab4.repository;

import com.github.tolisso.lab4.domain.Order;
import com.github.tolisso.lab4.domain.Visitor;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Flux<Order> getAllByVisitorName(String visitorName);
}
