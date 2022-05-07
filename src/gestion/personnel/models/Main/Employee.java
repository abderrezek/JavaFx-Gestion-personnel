package gestion.personnel.models.Main;

import java.sql.Date;
import javafx.scene.control.CheckBox;

public class Employee {
    
    private int Id;
    private String Nom;
    private String Prenom;
    private Date DateNaissance;
    private int Age;
    private String LieuNaissance;
    private String Adresse;
    private String Wilaya;
    private Genre Genre;
    private CheckBox Select = new CheckBox();
    
    public Employee() {        
    }

    public Employee(int Id, String Nom, String Prenom, Date DateNaissance, int Age, String LieuNaissance, String Adresse, String Wilaya, Genre Genre) {
        this.Id = Id;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.DateNaissance = DateNaissance;
        this.Age = Age;
        this.LieuNaissance = LieuNaissance;
        this.Adresse = Adresse;
        this.Wilaya = Wilaya;
        this.Genre = Genre;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setSelect(CheckBox chk) {
        Select = chk;
    }
    
    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }

    public void setDateNaissance(Date DateNaissance) {
        this.DateNaissance = DateNaissance;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }

    public void setLieuNaissance(String LieuNaissance) {
        this.LieuNaissance = LieuNaissance;
    }

    public void setAdresse(String Adresse) {
        this.Adresse = Adresse;
    }

    public void setWilaya(String Wilaya) {
        this.Wilaya = Wilaya;
    }

    public void setGenre(Genre genre) {
        this.Genre = genre;
    }

    public int getId() {
        return Id;
    }

    public CheckBox getSelect() {
        return Select;
    }

    public String getNom() {
        return Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public Date getDateNaissance() {
        return DateNaissance;
    }

    public int getAge() {
        return Age;
    }

    public String getLieuNaissance() {
        return LieuNaissance;
    }

    public String getAdresse() {
        return Adresse;
    }

    public String getWilaya() {
        return Wilaya;
    }

    public Genre getGenre() {
        return Genre;
    }

    @Override
    public String toString() {
        return "Employee{" + "Id=" + Id + ", Nom=" + Nom + ", Prenom=" + Prenom + ", DateNaissance=" + DateNaissance + ", Age=" + Age + ", LieuNaissance=" + LieuNaissance + ", Adresse=" + Adresse + ", Wilaya=" + Wilaya + ", Genre=" + Genre + '}';
    }
}
