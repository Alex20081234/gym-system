package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.TrainingNotFoundException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.Training;
import java.util.List;

public interface TrainingDao {
    void create(Training training) throws UserNotFoundException;
    Training select(int trainingId) throws TrainingNotFoundException;
    List<Training> selectAll() throws NoExpectedDataInDatabaseException;
}
