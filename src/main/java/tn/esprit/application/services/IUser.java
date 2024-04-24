package tn.esprit.application.services;

import java.util.List;

public interface IUser<T> {



        boolean UserExistsById(int id);
        boolean UserExistsByEmail(String email);
        T authenticateUser(String email, String password);
        boolean ajouterUser(T user);
        List<T> afficherUser();
        T getUserById(int id);
        void modifierUser(T user);
        void deleteUser(T user);
        int getIdByEmail(String email);
        String getRoleByEmail(String email);


}
