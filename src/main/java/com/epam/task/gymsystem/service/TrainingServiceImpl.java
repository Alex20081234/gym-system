package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.TrainingNotFoundException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.dao.TrainingDaoImpl;
import com.epam.task.gymsystem.domain.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDaoImpl dao;

    @Autowired
    public TrainingServiceImpl(TrainingDaoImpl dao) {
        this.dao = dao;
    }

    @Override
    public void create(Training training) throws UserNotFoundException {
        dao.create(training);
    }

    @Override
    public Training select(int id) throws TrainingNotFoundException {
        return dao.select(id);
    }

    @Override
    public List<Training> selectAll() throws NoExpectedDataInDatabaseException {
        return dao.selectAll();
    }
}
