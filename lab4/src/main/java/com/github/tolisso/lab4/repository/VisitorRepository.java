package com.github.tolisso.lab4.repository;

import com.github.tolisso.lab4.domain.Visitor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface VisitorRepository extends ReactiveCrudRepository<Visitor, String> {
    Mono<Visitor> findByName(String name);
}
