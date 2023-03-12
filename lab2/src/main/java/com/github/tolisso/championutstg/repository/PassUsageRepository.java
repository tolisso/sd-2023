package com.github.tolisso.championutstg.repository;

import com.github.tolisso.championutstg.model.PassUsageEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PassUsageRepository extends CrudRepository<PassUsageEntity, Long> {
    List<PassUsageEntity> getAllByUserName(String userName);
    List<PassUsageEntity> getAllByTimestampBetween(LocalDateTime from, LocalDateTime to);
    List<PassUsageEntity> getAllBy();
}
