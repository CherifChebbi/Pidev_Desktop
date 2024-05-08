package services;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ICategory <T> {
    public void ajouter (T t) throws SQLException;
    public void modifier (T t,int id) throws SQLException;
    public ObservableList<T> afficher () throws SQLException;
    public void supprimer (int categorie_id) throws SQLException;
}
