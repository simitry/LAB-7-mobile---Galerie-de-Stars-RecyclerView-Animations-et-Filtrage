package com.example.starsgallery.dao;

import java.util.List;

/*
 * INTERFACE DAO GENERIQUE
 * -----------------------
 * The service layer implements this contract.
 * The activity and adapter do not need to know how the data is stored.
 */
public interface IDao<T> {

    boolean create(T object);

    boolean update(T object);

    boolean delete(T object);

    T findById(int id);

    List<T> findAll();
}
