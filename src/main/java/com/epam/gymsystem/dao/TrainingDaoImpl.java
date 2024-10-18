package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.domain.*;
import com.epam.gymsystem.common.Dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
@AllArgsConstructor
public class TrainingDaoImpl implements TrainingDao {
    private final EntityManager entityManager;

    @Override
    public void create(Training training) {
        entityManager.persist(training);
    }

    @Override
    public Optional<Training> select(int trainingId) {
        try {
            return Optional.ofNullable(entityManager.createQuery("SELECT t FROM Training t WHERE t.id = :id", Training.class)
                    .setParameter("id", trainingId)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Training> selectAll() {
        return entityManager.createQuery("SELECT t FROM Training t", Training.class).getResultList();
    }

    @Override
    public List<Training> selectTrainings(String username, TrainingCriteria criteria) {
        Class<? extends User> userClass = getUserClass(username);
        Class<? extends User> partnerClass = getPartnerClass(userClass);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(training.get(userClass.getSimpleName().toLowerCase()).get("username"), username));
        if (criteria != null) {
            if (criteria.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), criteria.getFromDate()));
            }
            if (criteria.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), criteria.getToDate()));
            }
            if (criteria.getPartnerUsername() != null) {
                predicates.add(cb.equal(training.get(partnerClass.getSimpleName().toLowerCase()).get("username"), criteria.getPartnerUsername()));
            }
            if (criteria.getTrainingType() != null) {
                predicates.add(cb.equal(training.get("trainingType"), criteria.getTrainingType()));
            }
        }
        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }

    private Class<? extends User> getUserClass(String username) {
        Long count = entityManager.createQuery("SELECT COUNT(t) FROM Trainee t WHERE t.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        if (count > 0) {
            return Trainee.class;
        } else {
            count = entityManager.createQuery("SELECT COUNT(t) FROM Trainer t WHERE t.username = :username", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            if (count > 0) {
                return Trainer.class;
            } else {
                throw new UserNotFoundException("User with username " + username + " was not found");
            }
        }
    }

    private static Class<? extends User> getPartnerClass(Class<? extends User> className) {
        return className.equals(Trainee.class) ? Trainer.class : Trainee.class;
    }
}
