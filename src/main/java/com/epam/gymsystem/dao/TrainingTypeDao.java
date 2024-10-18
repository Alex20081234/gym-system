package com.epam.gymsystem.dao;

import com.epam.gymsystem.domain.TrainingType;
import java.util.List;
import java.util.Optional;

public interface TrainingTypeDao {
    List<TrainingType> selectAllTypes();

    Optional<TrainingType> selectType(String name);
}
