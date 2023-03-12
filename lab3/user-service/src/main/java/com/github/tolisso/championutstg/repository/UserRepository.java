package com.github.tolisso.championutstg.repository;

import com.github.tolisso.championutstg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getByName(String name);
}
