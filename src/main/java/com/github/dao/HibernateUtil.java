package com.github.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Copyright (C) Coderion sp. z o.o
 */
public class HibernateUtil {
    private static EntityManager entityManager;


    private HibernateUtil() {
    }

    public static EntityManager getEntityManager()
    {
        if(entityManager == null)
        {
            EntityManagerFactory entityManagerFactory = Persistence
                    .createEntityManagerFactory("pw");
            entityManager = entityManagerFactory
                    .createEntityManager();
        }

        return entityManager;
    }

}