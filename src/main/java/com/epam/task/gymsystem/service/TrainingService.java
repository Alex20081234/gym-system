package com.epam.task.gymsystem.service;


import com.epam.task.gymsystem.DAO.TrainingDAO;
import com.epam.task.gymsystem.domain.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {

    private final TrainingDAO dao;

    @Autowired
    public TrainingService(TrainingDAO dao) {
        this.dao = dao;
    }

    public void create(Training training) {
        dao.create(training);
    }

    public Training select(Long trainingId) {
        return dao.select(trainingId);
    }
}
