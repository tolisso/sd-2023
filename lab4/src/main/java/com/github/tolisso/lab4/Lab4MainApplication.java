package com.github.tolisso.lab4;

import com.github.tolisso.lab4.repository.OrderRepository;
import com.github.tolisso.lab4.repository.VisitorRepository;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
@RequiredArgsConstructor
public class Lab4MainApplication extends AbstractReactiveMongoConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(Lab4MainApplication.class, args);
    }

    @Override
    protected String getDatabaseName() {
        return "lab4";
    }
}
