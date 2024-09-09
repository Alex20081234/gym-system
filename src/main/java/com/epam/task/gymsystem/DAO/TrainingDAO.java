package com.epam.task.gymsystem.DAO;

import com.epam.task.gymsystem.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainingDAO {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDAO.class);

    private final Map<Long, Training> trainingMap;

    @Autowired
    public TrainingDAO(Map<Long, Training> trainingMap) {
        this.trainingMap = trainingMap;
    }

    public void create(Training training) {
        trainingMap.put(training.getTrainingId(), training);
        logger.info("Training was successfully created");
    }

    public Training select(Long trainingId) {
        return trainingMap.get(trainingId);
    }
}
