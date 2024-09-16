package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.domain.Trainer;
import java.util.Map;
import java.util.Optional;

public interface TrainerService {
    void create(Trainer trainer);
    void update(Long id, Trainer trainer);
    Optional<Trainer> select(Long id);
    Optional<Map<Long, Trainer>> selectAll();
}
