package com.epam.gymsystem.dao;

import com.epam.gymsystem.common.Dao;
import com.epam.gymsystem.common.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;

@Dao
@AllArgsConstructor
public class AuthDaoImpl implements AuthDao {
    private final EntityManager entityManager;

    @Override
    public String selectPassword(String username) {
        try {
            return entityManager.createQuery("select password from User where username = :username", String.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("User with username " + username + " was not found");
        }
    }
}
