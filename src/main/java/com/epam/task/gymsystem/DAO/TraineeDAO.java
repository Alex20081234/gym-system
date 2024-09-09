package com.epam.task.gymsystem.DAO;


import com.epam.task.gymsystem.domain.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TraineeDAO {

    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);

    private final Map<Long, Trainee> traineeMap;

    @Autowired
    public TraineeDAO(Map<Long, Trainee> traineeMap) {
        this.traineeMap = traineeMap;
    }

    public void create(Trainee trainee) {
        traineeMap.put(trainee.getUserId(), trainee);
        logger.info("Trainee was successfully created");
    }

    public void update(Trainee updated) {
        traineeMap.put(updated.getUserId(), updated);
        logger.info("Trainee was successfully updated");
    }

    public void delete(Long userId) {
        traineeMap.remove(userId);
        logger.info("Trainee was successfully deleted");
    }

    public Trainee select(Long userId) {
        return traineeMap.get(userId);
    }

    public Map<Long, Trainee> getTraineeMap() {
        return traineeMap;
    }
}
