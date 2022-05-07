package gestion.personnel.controllers;

import gestion.personnel.models.Main.*;
import gestion.personnel.Utils.Utils;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class AddController implements Initializable {
    
    @FXML
    private Button btnAddEdit, btnReset;    
    @FXML
    private Label lblNom, lblPrenom, lblDate, lblAge, lblLieu, lblAdresse, lblWilaya, lblGenre;
    @FXML
    private TextField txtNom, txtPrenom, txtAge, txtLieu, txtAdresse;
    @FXML
    private DatePicker dpDateNaissance;
    @FXML
    private ComboBox cbWilaya, cbCommune;
    @FXML
    private RadioButton rbMasculin, rbFemelle;
    
    private Employee employee;
    private MainController mainController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("controllers.AddController.initialize()");
        this.Tooltip();
        
        dpDateNaissance.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (dpDateNaissance.getValue() != null) {
                String age = Utils.getAge(newValue.getYear(), newValue.getMonth().getValue(), newValue.getDayOfMonth());
                txtAge.setText(age);
            }
        });
    }
    
    private void Tooltip() {
        btnAddEdit.setTooltip(new Tooltip("Ajouter employé"));
        btnReset.setTooltip(new Tooltip("Effacer tous les champs"));
    }
    
    public void setData(Object data) {
        if (data != null) {
            btnAddEdit.setText("Modifier");
            
            employee = (Employee) data;
            txtNom.setText(employee.getNom());
            txtPrenom.setText(employee.getPrenom());
            dpDateNaissance.setValue(employee.getDateNaissance().toLocalDate());
            txtAge.setText(String.valueOf(employee.getAge()));
            txtLieu.setText(employee.getLieuNaissance());
            txtAdresse.setText(employee.getAdresse());
            
            if (employee.getGenre() == Genre.Masculin) {
                rbMasculin.setSelected(true);
                rbFemelle.setSelected(false);
            } else if (employee.getGenre() == Genre.Femelle) {
                rbMasculin.setSelected(false);
                rbFemelle.setSelected(true);
            }
        }
        
    }
    
    public void setInstance(Object obj) {
        mainController = (MainController) obj;
    }
    
    @FXML
    public void LabelClickAction(Event e) {
        final Node source = (Node) e.getSource();
        String id = source.getId();
        switch (id) {
            case "lblNom":
                txtNom.requestFocus();
                break;
            case "lblPrenom":
                txtPrenom.requestFocus();
                break;
            case "lblDate":
                dpDateNaissance.requestFocus();
                break;
            case "lblAge":
                txtAge.requestFocus();
                break;
            case "lblLieu":
                txtLieu.requestFocus();
                break;
            case "lblAdresse":
                txtAdresse.requestFocus();
                break;
            case "lblWilaya":
                cbWilaya.requestFocus();
                break;
        }
    }
    
    private String FormValidation() {
        if (txtNom.getText().isEmpty()) {
            return "Nom est vide";
        } else if (txtPrenom.getText().isEmpty()) {
            return "Prenom est vide";
        } else if (dpDateNaissance.getValue() == null) {
            return "Date naissance est vide";
        } else if (txtLieu.getText().isEmpty()) {
            return "Lieu de naissance est vide";
        } else if (txtAdresse.getText().isEmpty()) {
            return "Adresse est vide";
        }
        return null;
    }
    
    @FXML
    public void btnAddEditAction(ActionEvent e) {
        if (this.FormValidation() != null) {
            Utils.ShowAlertMsg("Validation", this.FormValidation(), Alert.AlertType.ERROR, () -> {});
            return;
        }
        Employee emp = new Employee();
        emp.setNom(txtNom.getText());
        emp.setPrenom(txtPrenom.getText());
        Date date = Date.valueOf(dpDateNaissance.getValue());
        emp.setDateNaissance(date);
        emp.setAge(12);
        emp.setLieuNaissance(txtLieu.getText());
        emp.setAdresse(txtAdresse.getText());
        emp.setWilaya("w");
        if (rbMasculin.isSelected()) {
            emp.setGenre(Genre.Masculin);
        } else if (rbFemelle.isSelected()) {
            emp.setGenre(Genre.Femelle);
        }
        EmployeeDAO employeeDAO = new EmployeeDAO();
        
        Stage stage = (Stage) btnAddEdit.getScene().getWindow();
        
        if (btnAddEdit.getText().equals("Ajouter")) {
            if (employeeDAO.Insert(emp)) {
                mainController.LoadData(this.mainController.ItemsPerPage, -1);
                Utils.ShowAlertMsg("Ajouter", "Ajouté avec succès", Alert.AlertType.INFORMATION, () -> {
                    stage.close();
                });
            } else {
                Utils.ShowAlertMsg("Ajouter", "Il n'a pas été ajouté avec succès", Alert.AlertType.ERROR, () -> {});
            }
        } else if (btnAddEdit.getText().equals("Modifier")) {
            emp.setId(employee.getId());
            if (employeeDAO.Update(emp)) {
                mainController.LoadData(this.mainController.ItemsPerPage, -1);
                Utils.ShowAlertMsg("Modifier", "Modifier avec succès", Alert.AlertType.INFORMATION, () -> {
                    stage.close();
                });
            } else {
                Utils.ShowAlertMsg("Modifier", "Il n'a pas été modifier avec succès", Alert.AlertType.ERROR, () -> {});
            }
        }
    }
    
    @FXML
    public void btnResetAction(ActionEvent e) {
        Utils.ShowAlertYesNo("Effacer tous", "voulez-vous vraiment effacer tous les champs ?", () -> {
            txtAdresse.setText("");
            txtAge.setText("");
            txtLieu.setText("");
            txtNom.setText("");
            txtPrenom.setText("");
            dpDateNaissance.setValue(null);
            rbMasculin.setSelected(true);
            rbFemelle.setSelected(false);
            
            txtNom.requestFocus();
        });
    }

}
