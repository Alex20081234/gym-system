package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.dao.TrainingDao;
import com.epam.task.gymsystem.domain.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDao dao;
    private static final String NOT_VALID = "Training is not valid";

    @Autowired
    public TrainingServiceImpl(TrainingDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(Training training) {
        if (!isValid(training)) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        dao.create(training);
    }

    @Override
    public Training select(int id) {
        return dao.select(id);
    }

    @Override
    public List<Training> selectAll() {
        return dao.selectAll();
    }

    private boolean isValid(Training training) {
        return training != null && training.getTrainee() != null
                && training.getTrainer() != null && training.getTrainingName() != null
                && training.getTrainingType() != null && training.getTrainingDate() != null
                && training.getDuration() > 0;
    }
}
