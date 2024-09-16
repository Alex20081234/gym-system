package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.domain.Storage;
import com.epam.task.gymsystem.domain.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainerDao implements Creatable<Trainer>, Updatable<Trainer>, Selectable<Trainer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerDao.class);
    private final Map<Long, Trainer> trainerMap;

    @Autowired
    public TrainerDao(Storage<Trainer> storage) {
        trainerMap = storage.getMap();
    }

    @Override
    public void create(Trainer trainer) {
        trainerMap.put(trainer.getUserId(), trainer);
        LOGGER.info("Trainer was successfully created");
    }

    @Override
    public void update(Trainer updated) {
        trainerMap.put(updated.getUserId(), updated);
        LOGGER.info("Trainer was successfully updated");
    }

    @Override
    public Optional<Trainer> select(Long userId) {
        return Optional.ofNullable(trainerMap.get(userId));
    }

    @Override
    public Optional<Map<Long, Trainer>> selectAll() {
        return Optional.of(Collections.unmodifiableMap(trainerMap));
    }
}
