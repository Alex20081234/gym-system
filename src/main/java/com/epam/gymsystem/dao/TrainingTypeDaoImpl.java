package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.Dao;
import com.epam.gymsystem.domain.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Optional;

@Dao
@AllArgsConstructor
public class TrainingTypeDaoImpl implements TrainingTypeDao {
    private final EntityManager entityManager;

    @Override
    public List<TrainingType> selectAllTypes() {
        return entityManager.createQuery("SELECT t FROM TrainingType t", TrainingType.class).getResultList();
    }

    @Override
    public Optional<TrainingType> selectType(String name) {
        try {
            return Optional.ofNullable(entityManager.createQuery("SELECT t FROM TrainingType t WHERE t.name = :name", TrainingType.class)
                    .setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}
