package com.epam.gymsystem.service;

import com.epam.gymsystem.dao.AuthDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthDao authDao;

    @Override
    @Transactional(readOnly = true)
    public String selectPassword(String username) {
        return authDao.selectPassword(username);
    }
}
