package com.github.tolisso.championutstg.controller;

import com.github.tolisso.championutstg.model.Stock;
import com.github.tolisso.championutstg.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("stock")
public class ManagerController {
    private final StockRepository stockRepository;

    @PostMapping("/set")
    public void set(@RequestParam String name, @RequestParam double cost, @RequestParam int number) {
        stockRepository.save(new Stock(name, cost, number));
    }

    @PostMapping("/buy")
    public double buy(@RequestParam String name, @RequestParam int number) {
        Stock stock = stockRepository.getByName(name);
        if (stock.getNumber() < number) {
            throw new IllegalArgumentException();
        }
        stock.setNumber(stock.getNumber() - number);
        stockRepository.save(stock);
        return number * stock.getCost();
    }

    @PostMapping("/get")
    public Stock get(@RequestParam String name) {
        return stockRepository.getByName(name);
    }

    @PostMapping("/getAll")
    public List<Stock> getAll() {
        return stockRepository.findAll();
    }
}
