package com.epam.gymsystem.service;

import com.epam.gymsystem.dao.AuthDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthDao authDao;

    public AuthServiceImpl(AuthDao authDao) {
        this.authDao = authDao;
    }

    @Override
    @Transactional(readOnly = true)
    public String selectPassword(String username) {
        return authDao.selectPassword(username);
    }
}
