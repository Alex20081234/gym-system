package com.epam.gymsystem.service;

import com.epam.gymsystem.domain.User;
import com.epam.gymsystem.dto.Credentials;
import java.util.List;
import java.util.Optional;

public interface UserService<T extends User> {
    Credentials create(T user);

    void changePassword(String username, String newPassword);

    String update(String username, T updates);

    void changeActivityStatus(String username, boolean newActivityStatus);

    Optional<T> select(String username);

    List<T> selectAll();

    void loadDependencies(T user);
}
