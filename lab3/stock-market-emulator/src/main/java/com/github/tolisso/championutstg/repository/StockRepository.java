package com.github.tolisso.championutstg.repository;

import com.github.tolisso.championutstg.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock getByName(String name);
    List<Stock> findAll();
}
