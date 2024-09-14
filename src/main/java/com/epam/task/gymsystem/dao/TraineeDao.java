package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.domain.Storage;
import com.epam.task.gymsystem.domain.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Repository
public class TraineeDao implements Creatable<Trainee>, Updatable<Trainee>, Deletable<Trainee>, Selectable<Trainee> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeDao.class);
    private final Map<Long, Trainee> traineeMap;

    @Autowired
    public TraineeDao(Storage<Trainee> storage) {
        traineeMap = storage.getMap();
    }

    @Override
    public void create(Trainee trainee) {
        traineeMap.put(trainee.getUserId(), trainee);
        LOGGER.info("Trainee was successfully created");
    }

    @Override
    public void update(Trainee updated) {
        traineeMap.put(updated.getUserId(), updated);
        LOGGER.info("Trainee was successfully updated");
    }

    @Override
    public void delete(Long userId) {
        traineeMap.remove(userId);
        LOGGER.info("Trainee was successfully deleted");
    }

    @Override
    public Optional<Trainee> select(Long userId) {
        return Optional.ofNullable(traineeMap.get(userId));
    }

    @Override
    public Optional<Map<Long, Trainee>> selectAll() {
        return Optional.of(Collections.unmodifiableMap(traineeMap));
    }
}
