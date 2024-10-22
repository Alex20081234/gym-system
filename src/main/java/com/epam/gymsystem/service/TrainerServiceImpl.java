package com.epam.gymsystem.service;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.common.UserUtils;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.dao.TrainerDao;
import com.epam.gymsystem.dto.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private static final String NOT_VALID = "Trainer is not valid";
    private static final String NOT_FOUND = "Trainer with username %s was not found";
    private final TrainerDao dao;

    @Override
    @Transactional
    public Credentials create(Trainer trainer) {
        if (trainer == null) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        Credentials usernameAndPassword = UserUtils.setUsernameAndPassword(trainer, dao.selectUsernames());
        if (!isValid(trainer)) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        dao.create(trainer);
        return usernameAndPassword;
    }

    @Override
    @Transactional
    public void changePassword(String username, String newPassword) {
        if (newPassword == null || newPassword.length() < 8 || newPassword.length() > 20) {
            throw new IllegalArgumentException("New password is not valid");
        }
        dao.changePassword(username, newPassword);
    }

    @Override
    @Transactional
    public String update(String username, Trainer updates) {
        if (updates == null) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        Trainer trainer = dao.select(username).orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, username)));
        return dao.update(username, (Trainer) UserUtils.mergeUsers(trainer, updates, dao.selectUsernames()));
    }

    @Override
    @Transactional
    public void changeActivityStatus(String username, boolean newActivityStatus) {
        Trainer trainer = dao.select(username).orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, username)));
        if (trainer.getIsActive() == newActivityStatus) {
            throw new IllegalArgumentException("Activity status is already " + newActivityStatus);
        }
        dao.changeActivityStatus(username, newActivityStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> select(String username) {
        return dao.select(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> selectAll() {
        return dao.selectAll();
    }

    @Override
    @Transactional(readOnly = true)
    public void loadDependencies(Trainer trainer) {
        dao.loadDependencies(trainer);
    }

    private boolean isValid(Trainer trainer) {
        return trainer.getIsActive() != null && trainer.getSpecialization() != null;
    }
}
