package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.domain.Storage;
import com.epam.task.gymsystem.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainingDao implements Creatable<Training>, Selectable<Training> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingDao.class);
    private final Map<Long, Training> trainingMap;

    @Autowired
    public TrainingDao(Storage<Training> storage) {
        trainingMap = storage.getMap();
    }

    @Override
    public void create(Training training) {
        trainingMap.put(training.getTrainingId(), training);
        LOGGER.info("Training was successfully created");
    }

    @Override
    public Optional<Training> select(Long trainingId) {
        return Optional.ofNullable(trainingMap.get(trainingId));
    }

    @Override
    public Optional<Map<Long, Training>> selectAll() {
        return Optional.of(Collections.unmodifiableMap(trainingMap));
    }
}
