package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.dao.TrainingDao;
import com.epam.task.gymsystem.domain.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final String NOT_VALID = "Training is not valid";
    private final TrainingDao dao;

    @Autowired
    public TrainingServiceImpl(TrainingDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public void create(Training training) {
        if (!isValid(training)) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        dao.create(training);
    }

    @Override
    @Transactional(readOnly = true)
    public Training select(int id) {
        return dao.select(id);
    }

    @Override
    @Transactional(readOnly = true)
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
