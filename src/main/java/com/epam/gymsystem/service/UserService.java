package com.epam.gymsystem.service;

import com.epam.gymsystem.domain.User;
import com.epam.gymsystem.dto.UsernameAndPassword;
import java.util.List;

public interface UserService<T extends User> {
    UsernameAndPassword create(T user);

    void changePassword(String username, String newPassword);

    String update(String username, T updates);

    void changeActivityStatus(String username, boolean newActivityStatus);

    T select(String username);

    List<T> selectAll();

    void loadDependencies(T user);
}
