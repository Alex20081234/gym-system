package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.dao.TrainerDao;
import com.epam.task.gymsystem.domain.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
    private final TrainerDao dao;

    @Autowired
    public TrainerServiceImpl(TrainerDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(Trainer trainer) {
        UserService.setUsernameAndPassword(trainer, selectAll().get());
        dao.create(trainer);
    }

    @Override
    public void update(Long userId, Trainer changes) {
        Optional<Trainer> trainer = dao.select(userId);
        boolean nameChanged = false;
        if (changes.getUserId() != null) {
            trainer.get().setUserId(changes.getUserId());
        }
        if (changes.getSpecialization() != null) {
            trainer.get().setSpecialization(changes.getSpecialization());
        }
        if (changes.getFirstName() != null) {
            trainer.get().setFirstName(changes.getFirstName());
            nameChanged = true;
        }
        if (changes.getLastName() != null) {
            trainer.get().setLastName(changes.getLastName());
            nameChanged = true;
        }
        if (nameChanged) {
            trainer.get().setUsername(UserService.generateUsername(trainer.get().getFirstName(), trainer.get().getLastName(), selectAll().get()));
        }
        if (changes.getPassword() != null) {
            trainer.get().setPassword(changes.getPassword());
        }
        trainer.get().setActive(changes.isActive());
        dao.update(trainer.get());
    }

    @Override
    public Optional<Trainer> select(Long userId) {
        return dao.select(userId);
    }

    @Override
    public Optional<Map<Long, Trainer>> selectAll() {
        return dao.selectAll();
    }
}
