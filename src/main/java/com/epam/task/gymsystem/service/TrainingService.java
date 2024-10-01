package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.common.TrainingNotFoundException;
import com.epam.task.gymsystem.domain.Training;
import java.util.List;

public interface TrainingService {
    void create(Training training);
    Training select(int id) throws TrainingNotFoundException;
    List<Training> selectAll();
}
