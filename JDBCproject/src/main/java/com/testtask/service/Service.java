package com.testtask.service;

import com.testtask.exception.DataException;

/**
 * Интрефейс c методами по тестовому заданию "Газинформсервис".
 *
 * @param <T> - тип данных для работы сервиса
 */
public interface Service<T> {
    T getAccountByFirstName(String firstName) throws DataException;

    void updateLastName(String firstName, String lastName) throws DataException;
}