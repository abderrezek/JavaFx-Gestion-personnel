package gestion.personnel;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gestion.personnel.models.Main.SeederEmployee;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("views/Main.fxml"));
            
            Scene scene = new Scene(root);

            primaryStage.setTitle("JavaFX - CRUD");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception ex) {
            System.out.println("Main.start() FXML not Load");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        // SeederEmployee.run(200);
        launch(args);
    }
    
}
