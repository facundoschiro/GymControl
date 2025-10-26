package ar.edu.gymcontrol.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML private StackPane contentPane;

    @FXML
    public void initialize() {
        loadView("/ar/edu/gymcontrol/view/AccesoView.fxml"); // pantalla inicial
    }

    @FXML private void goAcceso()    { loadView("/ar/edu/gymcontrol/view/AccesoView.fxml"); }
    @FXML private void goSocios()     { loadView("/ar/edu/gymcontrol/view/SociosView.fxml"); }
    @FXML private void goEntrenos() { loadView("/ar/edu/gymcontrol/view/EntrenosView.fxml"); }
    @FXML private void goEjercicios() { loadView("/ar/edu/gymcontrol/view/EjerciciosView.fxml"); }
    @FXML private void goReportes() { loadView("/ar/edu/gymcontrol/view/ReportesView.fxml"); }

    @FXML private void exitApp() { contentPane.getScene().getWindow().hide(); }

    private void loadView(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
