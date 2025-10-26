package ar.edu.gymcontrol.controller;

import ar.edu.gymcontrol.service.ServiceRegistry;
import ar.edu.gymcontrol.service.ValidacionAcceso;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;

public class AccesoController {

    @FXML private TextField dniField;
    @FXML private Label resultadoLabel;
    @FXML private ListView<String> ultimosList;

    private final Deque<String> ultimos = new ArrayDeque<>(10);

    @FXML
    public void initialize() {
        resultadoLabel.setText("Esperando DNI...");
    }

    @FXML
    private void validarAcceso() {
        String dni = dniField.getText() == null ? "" : dniField.getText().trim();
        try {
            if (!dni.matches("\\d{7,8}")) throw new IllegalArgumentException("DNI inválido (7 u 8 dígitos).");

            ValidacionAcceso res = ServiceRegistry.get().servicioAcceso.validarPorDni(dni);
            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
            String item = ts + " • DNI " + dni + " → " + (res.permitido() ? "PERMITIDO" : "DENEGADO") + " (" + res.mensaje() + ")";

            if (ultimos.size() == 10) ultimos.removeFirst();
            ultimos.addLast(item);
            ultimosList.getItems().setAll(ultimos);

            resultadoLabel.setText(res.permitido() ? "✅ Acceso PERMITIDO" : "⛔ " + res.mensaje());
        } catch (Exception ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        } finally {
            dniField.requestFocus();
            dniField.selectAll();
        }
    }
}
