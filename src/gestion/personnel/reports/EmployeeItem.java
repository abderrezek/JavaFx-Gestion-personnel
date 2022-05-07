package gestion.personnel.reports;

public class EmployeeItem {
    
    private String nomPrenom;
    private String dateNaissance;
    private String lieuNaissance;
    private String genre;
    private String wilaya;

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public String getGenre() {
        return genre;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }
    
}
