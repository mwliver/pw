package com.github.dao;

import com.github.model.Directory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Copyright (C) Coderion sp. z o.o
 */
public class DirectoryDao {

    private static final Logger logger = Logger.getLogger(DirectoryDao.class
            .getName());
    private static EntityManager entityManager = HibernateUtil.getEntityManager();

    public static void createDirectory(Directory directory) {
        entityManager.getTransaction().begin();

        entityManager.persist(directory);

        entityManager.getTransaction().commit();

        logger.info("Just added new directory");

    }

    public static List<Directory> findAll() {
        entityManager.getTransaction().begin();

        List<Directory> directories = new ArrayList<>();
        Query query = entityManager.createQuery("from Directory");
        directories = query.getResultList();
        entityManager.getTransaction().commit();

        logger.info("Received all directories");

        return directories;
    }
    public static Directory findDirectory(Long id) {
        return entityManager.find(Directory.class, id);

    }

    public static void removeDirectory(Long id) {
        entityManager.getTransaction().begin();
        Directory directory = findDirectory(id);
        System.out.println("Deleting        : " + directory.getId());
        if (directory != null)
            entityManager.remove(directory);
        entityManager.getTransaction().commit();
    }
}
