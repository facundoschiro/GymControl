package ar.edu.gymcontrol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ar/edu/gymcontrol/view/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/ar/edu/gymcontrol/style/app.css").toExternalForm());
        stage.setTitle("GymControl+ (MVP)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}
