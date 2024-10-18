package com.epam.gymsystem.service;

import com.epam.gymsystem.dao.TrainingTypeDao;
import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingCriteria;
import com.epam.gymsystem.domain.TrainingType;
import com.epam.gymsystem.dao.TrainingDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private static final String NOT_VALID = "Training is not valid";
    private final TrainingDao dao;
    private final TrainingTypeDao typeDao;

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
    public Optional<Training> select(int id) {
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
        return typeDao.selectAllTypes();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingType> selectType(String name) {
        return typeDao.selectType(name);
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
