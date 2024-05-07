package models.PlatEntity;

import models.RestaurantEntity.Restaurant;

public class Plat {
    private int id;
    private String nom;
    private String image;
    private float prix;
    private int restaurant_id;
    private Restaurant restaurant;

    // Existing constructors...

    // Add a constructor that accepts a Restaurant object
    public Plat(int id, String nom, String image, float prix, Restaurant restaurant) {
        this.id = id;
        this.nom = nom;
        this.image = image;
        this.prix = prix;
        this.restaurant = restaurant;
    }

    // Getter and setter for restaurant
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Plat(int id, String nom, String image, float prix, int restaurant_id) {
        this.id = id;
        this.nom = nom;
        this.image = image;
        this.prix = prix;
        this.restaurant_id = restaurant_id;
    }

    public Plat(String nom, String image, float prix, int restaurant_id) {
        this.nom = nom;
        this.image = image;
        this.prix = prix;
        this.restaurant_id = restaurant_id;
    }

    public Plat() {
    }

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
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Nom cannot be empty");
        }

        this.nom = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    @Override
    public String toString() {
        return "Plat{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", image='" + image + '\'' +
                ", prix=" + prix +
                ", restaurant_id=" + restaurant_id +
                '}';
    }
}
