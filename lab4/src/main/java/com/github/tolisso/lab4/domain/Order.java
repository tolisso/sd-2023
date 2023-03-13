package com.github.tolisso.lab4.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
public class Order {
    private static long idIncrementor = 0;

    public static synchronized long getNewId() {
        idIncrementor++;
        return idIncrementor;
    }

    @Id
    private Long orderId;
    private String visitorName;
    private String orderName;
    private Double price;

    public Order(String visitorName, String orderName, Double price) {
        this.orderId = getNewId();
        this.visitorName = visitorName;
        this.orderName = orderName;
        this.price = price;
    }

    public Order() {
        this.orderId = getNewId();
    }
}
