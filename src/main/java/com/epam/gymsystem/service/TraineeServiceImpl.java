package com.epam.gymsystem.service;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.common.UserUtils;
import com.epam.gymsystem.dao.TraineeDao;
import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.dto.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private static final String NOT_VALID = "Trainee is not valid";
    private static final String NOT_FOUND = "Trainee with username %s was not found";
    private final TraineeDao dao;
    private PasswordEncoder encoder;

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> selectNotAssignedTrainers(String username) {
        Trainee trainee = dao.select(username).orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, username)));
        return dao.selectNotAssignedTrainers(trainee);
    }

    @Override
    @Transactional
    public void updateTrainers(String username, Map<String, Boolean> trainerUsernames) {
        if (trainerUsernames == null) {
            throw new IllegalArgumentException("Trainers are not valid");
        }
        Trainee trainee = dao.select(username).orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, username)));
        dao.updateTrainers(trainee, trainerUsernames);
    }

    @Override
    @Transactional
    public void delete(String username) {
        dao.delete(username);
    }

    @Override
    @Transactional
    public Credentials create(Trainee trainee) {
        if (trainee == null) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        Credentials credentials = UserUtils.setUsernameAndPassword(trainee, dao.selectUsernames());
        String encoded = encoder.encode(trainee.getPassword());
        trainee.setPassword(encoded);
        if (!isValid(trainee)) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        dao.create(trainee);
        return credentials;
    }

    @Override
    @Transactional
    public void changePassword(String username, String newPassword) {
        if (newPassword == null || newPassword.length() < 8 || newPassword.length() > 20) {
            throw new IllegalArgumentException("New password is not valid");
        }
        String encoded = encoder.encode(newPassword);
        dao.changePassword(username, encoded);
    }

    @Override
    @Transactional
    public String update(String username, Trainee updates) {
        if (updates == null) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        Trainee trainee = dao.select(username).orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, username)));
        String password = updates.getPassword();
        String encoded = password;
        if (password != null) {
            encoded = encoder.encode(password);
        }
        updates.setPassword(encoded);
        return dao.update(username, (Trainee) UserUtils.mergeUsers(trainee, updates, dao.selectUsernames()));
    }

    @Override
    @Transactional
    public void changeActivityStatus(String username, boolean newActivityStatus) {
        Trainee trainee = dao.select(username).orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND, username)));
        if (trainee.getIsActive() == newActivityStatus) {
            throw new IllegalArgumentException("Activity status is already " + newActivityStatus);
        }
        dao.changeActivityStatus(username, newActivityStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> select(String username) {
        return dao.select(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> selectAll() {
        return dao.selectAll();
    }

    @Override
    @Transactional(readOnly = true)
    public void loadDependencies(Trainee trainee) {
        dao.loadDependencies(trainee);
    }

    private boolean isValid(Trainee trainee) {
        return trainee.getIsActive() != null;
    }
}
