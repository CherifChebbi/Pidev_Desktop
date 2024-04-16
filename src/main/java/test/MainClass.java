package test;

import models.Pays;
import services.ServicePays;
import utils.DBConnexion;

import java.sql.SQLException;

public class MainClass {

    public static void main(String[] args) {
        DBConnexion cn1 = DBConnexion.getInstance();

        Pays p = new Pays("aa","eee","aaa","aaa","aaa",12,15,15);

        ServicePays sp = new ServicePays();

        try {
            sp.Add(p);
            System.out.println(sp.Afficher());
        } catch (SQLException e) {
            System.err.println("Erreur: "+e.getMessage());
        }


    }
}
