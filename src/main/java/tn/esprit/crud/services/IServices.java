package tn.esprit.crud.services;

import tn.esprit.crud.models.User;

import java.sql.SQLException;
import java.util.List;

public interface IServices<T> {
    void ajouter (T t) throws SQLException;

    void modifier (T t) throws SQLException;

    void supprimer (T t) throws SQLException;

    void supprimer(int id) throws SQLException;


    List<T> recuperer() throws SQLException;

    void inscription(User user) throws SQLException;
}
