package com.github.tolisso.championutstg.repository;

import com.github.tolisso.championutstg.model.User;
import com.github.tolisso.championutstg.model.UserStock;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    Optional<UserStock> findByUserNameAndStockName(String userName, String stockName);
    List<UserStock> getAllByUserName(String userName);
}
