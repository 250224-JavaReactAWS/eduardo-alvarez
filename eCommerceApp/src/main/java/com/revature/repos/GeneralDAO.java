package com.revature.repos;

public interface GeneralDAO <T> {
    T create(T obj);

    T getByID(int userID);

    T update(T obj);
}
