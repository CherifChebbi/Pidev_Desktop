package tn.esprit.application.models;

public class User {
    private int id;
    private String email;
    private String role;
    private String password;
    private String nom;
    private String prenom;
    private int numtel;
    private String nationnalite; // corrected attribute name
    private boolean isBanned;
    private boolean isVerified;
    private String profilePicture;

    public User(int id, String email, String role, String password, String nom, String prenom, int numtel, String nationnalite, boolean isBanned, boolean isVerified, String profilePicture) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.numtel = numtel;
        this.nationnalite = nationnalite;
        this.isBanned = isBanned;
        this.isVerified = isVerified;
        this.profilePicture = profilePicture;
    }

    public User() {

    }

    public User(String text, String generatedString, String image, Role selectedRole, boolean b, String text1, String text2, int i) {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getNumtel() {
        return numtel;
    }

    public void setNumtel(int numtel) {
        this.numtel = numtel;
    }

    public String getNationnalite() {
        return nationnalite;
    }

    public void setNationnalite(String nationnalite) {
        this.nationnalite = nationnalite;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", numtel=" + numtel +
                ", nationnalite='" + nationnalite + '\'' +
                ", isBanned=" + isBanned +
                ", isVerified=" + isVerified +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}
