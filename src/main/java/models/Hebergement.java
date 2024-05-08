package models;

public class Hebergement {
    int id;
    int categorie_id;
    String nom;
    String description;

    private CategoryH restaurant;

    public CategoryH getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(CategoryH restaurant) {
        this.restaurant = restaurant;
    }





    public Hebergement() {}

    public Hebergement(int categorie_id, String nom, String description, CategoryH categoryH) {
        this.categorie_id = categorie_id;
        this.nom = nom;
        this.description = description;
        this.restaurant = categoryH;
    }

    public Hebergement(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(int categorie_id) {
        this.categorie_id = categorie_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Hebergement{" +
                "id=" + id +
                ", categorie_id=" + categorie_id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

