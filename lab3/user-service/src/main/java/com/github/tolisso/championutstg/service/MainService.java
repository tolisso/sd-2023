package com.github.tolisso.championutstg.service;

import com.github.tolisso.championutstg.dto.Stock;
import com.github.tolisso.championutstg.feign.StockMarket;
import com.github.tolisso.championutstg.model.User;
import com.github.tolisso.championutstg.model.UserStock;
import com.github.tolisso.championutstg.repository.UserRepository;
import com.github.tolisso.championutstg.repository.UserStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {
    public final UserRepository userRepository;
    public final UserStockRepository userStockRepository;
    public final StockMarket stockMarket;

    public void createUser(String userName) {
        userRepository.save(new User(userName, 0));
    }

    public void addMoney(String userName, double amount) {
        User user = userRepository.getByName(userName);
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
    }

    public List<UserStock> getAll(String userName) {
        return userStockRepository.getAllByUserName(userName);
    }

    public double sum(String userName) {
        var stocks = stockMarket.getAll().stream()
                .collect(Collectors.toMap(Stock::getName, Stock::getCost));
        return userStockRepository.getAllByUserName(userName).stream()
                .mapToDouble(userStock -> userStock.getNumber() * stocks.get(userStock.getStockName()))
                .sum() + userRepository.getByName(userName).getBalance();
    }

    public void buy(String userName, String stockName, int amount) {
        double spent = stockMarket.buy(stockName, amount);

        if (userStockRepository.findByUserNameAndStockName(userName, stockName).isEmpty()) {
            userStockRepository.save(new UserStock(userName, stockName, 0));
        }
        var stock = userStockRepository.findByUserNameAndStockName(userName, stockName).get();
        stock.setNumber(stock.getNumber() + amount);
        userStockRepository.save(stock);

        var user = userRepository.getByName(userName);
        user.setBalance(user.getBalance() - spent);
        userRepository.save(user);
    }
}
