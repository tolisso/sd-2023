package com.github.tolisso.championutstg.feign;

import com.github.tolisso.championutstg.dto.Stock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface StockMarket {

    @PostMapping("/set")
    void set(@RequestParam String name, @RequestParam double cost, @RequestParam int number);

    @PostMapping("/buy")
    Double buy(@RequestParam String name, @RequestParam int number);

    @PostMapping("/get")
    Stock get(@RequestParam String name);

    @PostMapping("/getAll")
    List<Stock> getAll();
}
