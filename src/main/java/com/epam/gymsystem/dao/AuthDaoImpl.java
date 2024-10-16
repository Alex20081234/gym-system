package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.Dao;
import com.epam.gymsystem.common.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;

@Dao
public class AuthDaoImpl implements AuthDao {
    private final EntityManager entityManager;

    @Autowired
    public AuthDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String selectPassword(String username) {
        String password;
        try {
            password = entityManager.createQuery("select password from User where username = :username", String.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("User with username " + username + " was not found");
        }
        return password;
    }
}
