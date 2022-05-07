package gestion.personnel.Utils;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public final class Utils {
    
    private Utils() { }
    
    private static Method GetMethod(Class cls, String nomM, Class... paramsM) {
        try {
            return cls.getMethod(nomM, paramsM);
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static void ShowNewWindow(String fxml, Object obj, String title, Object data) {
        if (fxml.isEmpty() || title.isEmpty()) {
            System.err.println("In => Utils.Utils.ShowNewWindow()");
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(obj.getClass().getClassLoader().getResource(fxml));
            Parent root = loader.load();
            
            Object controller = loader.getController();
            Class cls = controller.getClass();
            // set object for update TableView
            Method methodSetInstance = GetMethod(cls, "setInstance", Object.class);
            if (methodSetInstance != null) {
                methodSetInstance.invoke(controller, obj);
            }
            // set data to controller example when edit
            if (data != null) {
                Method methodSetData = GetMethod(cls, "setData", Object.class);
                if (methodSetData != null) {
                    methodSetData.invoke(controller, data);
                }
            }
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Utils.Utils.ShowNewWindow()");
            e.printStackTrace();
        }
    }

    public static void ShowAlertYesNo(String title, String desc, ActionLambda al) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(desc);
        ButtonType Oui = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType Non = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(Oui, Non);
        alert.showAndWait().ifPresent(type -> {
            if (type == Oui) { al.run(); }
        });
    }
    
    public static void ShowAlertMsg(String title, String desc, Alert.AlertType typeAlert, ActionLambda al) {
        Alert alert = new Alert(typeAlert);
        alert.setTitle(title);
        alert.setContentText(desc);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.OK) { al.run(); }
        });
    }
    
    /**
    * Method to extract the user's age from the entered Date of Birth.
    * 
    * @param year int year from date of birth
    * @param month int month from date of birth
    * @param day int day from date of birth
    * 
    * @return ageS String The user's age in years based on the supplied DoB.
    */
    public static String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day); 

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--; 
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;  
    }

    public interface ActionLambda {
        void run();
    }

    
    /**
     * Check if String is date
     * @param s String
     * @return boolean
     */
    public static boolean isDate(String s) {
        try {
            LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Check if String is integer
     * @param s String
     * @return boolean
     */
    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) { 
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
    
}
