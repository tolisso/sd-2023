package com.github.tolisso.championutstg;

import com.github.tolisso.championutstg.feign.StockMarket;
import com.github.tolisso.championutstg.service.MainService;
import feign.Feign;
import feign.Target;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
@SpringBootTest
@Import(MainServiceTest.TestConfigure.class)
class MainServiceTest {

    @Container
    private static final GenericContainer stockMarketContainer = new GenericContainer<>("docker.io/library/lab2:0.0.1-SNAPSHOT").withExposedPorts(9017);

    @TestConfiguration
    public static class TestConfigure {

        @Bean
        public StockMarket stockMarket() {
            String url = "http://" + stockMarketContainer.getHost() + ":" + stockMarketContainer.getFirstMappedPort() + "/stock";
            ;
            return Feign.builder()
                    .contract(new SpringMvcContract())
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .target(new Target.HardCodedTarget<>(StockMarket.class, "stock-market", url));
        }
    }

    @Lazy
    @Autowired
    private MainService mainService;

    @Lazy
    @Autowired
    private StockMarket stockMarket;

    @Test
    void test() {
        stockMarket.set("a", 3, 100);
        mainService.createUser("Ann");
        mainService.addMoney("Ann", 400);
        mainService.buy("Ann", "a", 100);
        stockMarket.set("a", 4, 0);
        assertEquals(500.0, mainService.sum("Ann"));
        var userStock = mainService.getAll("Ann");
        assertEquals(1, userStock.size());
        assertEquals(100, userStock.get(0).getNumber());
    }

}
