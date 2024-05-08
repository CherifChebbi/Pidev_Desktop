package models;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String nationnalite;
    private String email;
    private String password;
    private String roles;
    private int numtel;
    private boolean isBanned; // New attribute

    public User(int id, String nom, String prenom, String nationnalite, String email, String password, String roles, int numtel, boolean isBanned) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.nationnalite = nationnalite;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.numtel = numtel;
        this.isBanned = isBanned;
    }

    public User() {
    }

    public User(String nom, String prenom, String nationnalite, String email, String password, String roles, int numtel, boolean isBanned) {
        this.nom = nom;
        this.prenom = prenom;
        this.nationnalite = nationnalite;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.numtel = numtel;
        this.isBanned = isBanned;
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
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNationnalite() {
        return nationnalite;
    }

    public void setNationnalite(String nationnalite) {
        this.nationnalite = nationnalite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public int getNumtel() {
        return numtel;
    }

    public void setNumtel(int numtel) {
        this.numtel = numtel;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
    public void setIsBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nationnalite='" + nationnalite + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", numtel=" + numtel +
                ", isBanned=" + isBanned +
                '}';
    }
}
