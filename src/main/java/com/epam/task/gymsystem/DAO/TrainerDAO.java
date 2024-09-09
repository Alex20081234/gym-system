package com.epam.task.gymsystem.DAO;


import com.epam.task.gymsystem.domain.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainerDAO {

    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);

    private final Map<Long, Trainer> trainerMap;

    @Autowired
    public TrainerDAO(Map<Long, Trainer> trainerMap) {
        this.trainerMap = trainerMap;
    }

    public void create(Trainer trainer) {
        trainerMap.put(trainer.getUserId(), trainer);
        logger.info("Trainer was successfully created");
    }

    public void update(Trainer updated) {
        trainerMap.put(updated.getUserId(), updated);
        logger.info("Trainer was successfully updated");
    }

    public void delete(Long userId) {
        trainerMap.remove(userId);
        logger.info("Trainer was successfully deleted");
    }

    public Trainer select(Long userId) {
        return trainerMap.get(userId);
    }

    public Map<Long, Trainer> getTrainerMap() {
        return trainerMap;
    }
}
