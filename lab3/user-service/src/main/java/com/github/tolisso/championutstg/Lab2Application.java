package com.github.tolisso.championutstg;

import com.github.tolisso.championutstg.feign.StockMarket;
import com.github.tolisso.championutstg.feign.StockMarketSettings;
import feign.Feign;
import feign.Target;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@Log4j2
@SpringBootApplication
@EnableFeignClients
public class Lab2Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab2Application.class, args);
    }
}
