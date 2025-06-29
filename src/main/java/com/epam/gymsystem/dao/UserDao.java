package com.epam.gymsystem.dao;

import com.epam.gymsystem.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserDao<T extends User> {
    void create(T user);

    void changePassword(String username, String newPassword);

    String update(String username, T updated);

    void changeActivityStatus(String username, boolean newActivityStatus);

    Optional<T> select(String username);

    List<T> selectAll();

    List<String> selectUsernames();

    void loadDependencies(T user);
}
