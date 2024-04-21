package entities;

public class Category {
    private int id;
    private String nom;

    public Category(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom; // Retourne le nom de la catégorie
    }

}
