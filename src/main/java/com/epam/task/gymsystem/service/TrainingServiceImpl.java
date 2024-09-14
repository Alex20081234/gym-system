package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.dao.TrainingDao;
import com.epam.task.gymsystem.domain.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDao dao;

    @Autowired
    public TrainingServiceImpl(TrainingDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(Training training) {
        dao.create(training);
    }

    @Override
    public Optional<Training> select(Long trainingId) {
        return dao.select(trainingId);
    }
}
