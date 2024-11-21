package com.epam.gymsystem.dao;

import com.epam.gymsystem.domain.Token;

public interface BlacklistDao {
    void blacklist(Token token);

    boolean isBlacklisted(String token);
}
