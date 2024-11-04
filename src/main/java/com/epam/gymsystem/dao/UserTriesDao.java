package com.epam.gymsystem.dao;

public interface UserTriesDao {
    void incrementAttemptsOrCreateEntry(String username);

    void block(String username);

    boolean isBlocked(String username);

    int attempts(String username);
}
