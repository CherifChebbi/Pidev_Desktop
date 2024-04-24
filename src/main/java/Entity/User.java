package Entity;

public class User {
    private int id;
    private String email;
    private String roles;
    private String password;
    private  String nom;
    private String prenom;
    private String numtel;
    private String nationnalite ;
    private boolean is_banned;
    private boolean is_verified;

    public User(String nom, String prenom, String phone, String nationnalite, String email, String password) {
    }

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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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

    public String getNumtel() {
        return numtel;
    }

    public void setNumtel(String numtel) {
        this.numtel = numtel;
    }

    public String getNationnalite() {
        return nationnalite;
    }

    public void setNationnalite(String nationnalite) {
        this.nationnalite = nationnalite;
    }

    public boolean isIs_banned() {
        return is_banned;
    }

    public void setIs_banned(boolean is_banned) {
        this.is_banned = is_banned;
    }

    public boolean isIs_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public User() {
    }

    public User(int id, String email, String roles, String password, String nom, String prenom, String numtel, String nationnalite, boolean is_banned, boolean is_verified) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.numtel = numtel;
        this.nationnalite = nationnalite;
        this.is_banned = is_banned;
        this.is_verified = is_verified;
    }

    public User( int id,String email, String password, String nom, String prenom, String numtel, String nationnalite) {

        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.numtel = numtel;
        this.nationnalite = nationnalite;
    }

    public User(int id, String email, String roles, String password, String nom, String prenom, String numtel, String nationnalite) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.numtel = numtel;
        this.nationnalite = nationnalite;

    }
    public User(String email, String roles, String password, String nom, String prenom, String numtel, String nationnalite) {

        this.email = email;
        this.roles = roles;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.numtel = numtel;
        this.nationnalite = nationnalite;

    }

    

   /* public User(int id, String email, String roles, String password, String nom, String prenom, long numtel, String nationnalite) {
       this.id = id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.numtel = numtel;
        this.nationnalite = nationnalite;

    }*/



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", roles='" + roles + '\'' +
                ", password='" + password + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", numtel=" + numtel +
                ", nationnalite='" + nationnalite + '\'' +
                ", is_banned=" + is_banned +
                ", is_verified=" + is_verified +
                '}';
    }

    //contrôle de saisi
}
