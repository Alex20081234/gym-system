package com.epam.gymsystem.service;

import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingCriteria;
import com.epam.gymsystem.domain.TrainingType;
import com.epam.gymsystem.dao.TrainingDao;
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

    @Override
    @Transactional(readOnly = true)
    public List<TrainingType> selectAllTypes() {
        return dao.selectAllTypes();
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingType selectType(String name) {
        return dao.selectType(name);
    }

    @Override
    @Transactional
    public List<Training> selectTrainings(String username, TrainingCriteria criteria) {
        return dao.selectTrainings(username, criteria);
    }

    private boolean isValid(Training training) {
        return training != null && training.getTrainee() != null
                && training.getTrainer() != null && training.getTrainingName() != null
                && training.getTrainingType() != null && training.getTrainingDate() != null
                && training.getDuration() > 0;
    }
}
