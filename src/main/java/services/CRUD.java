package services;

import java.sql.SQLException;
import java.util.List;
public interface CRUD<T> {

    void Add(T t) throws SQLException;
    void Update(T t) throws SQLException;
    void Delete(T t) throws SQLException;
    List<T> Afficher() throws SQLException;
}
