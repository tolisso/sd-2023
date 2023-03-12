package com.github.tolisso.championutstg.repository;

import com.github.tolisso.championutstg.model.PassEntity;
import com.github.tolisso.championutstg.model.PassUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassRepository extends JpaRepository<PassEntity, Long> {
    List<PassEntity> getAllByPassId(Long passId);
    List<PassEntity> getAllByUserName(String userName);
}
