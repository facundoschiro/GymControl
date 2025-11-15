package ar.edu.gymcontrol.controller;

import ar.edu.gymcontrol.service.ServiceRegistry;
import ar.edu.gymcontrol.util.RingBuffer;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccesoController {

    @FXML private TextField dniField;
    @FXML private Label resultadoLabel;
    @FXML private ListView<String> ultimosList;

    // Buffer circular: ya expulsa el más viejo cuando se llena (no hay removeFirst)
    private final RingBuffer<String> ultimos = new RingBuffer<>(10);

    @FXML
    public void initialize() { resultadoLabel.setText("Esperando DNI..."); }

    @FXML
    private void validarAcceso() {
        String dni = dniField.getText() == null ? "" : dniField.getText().trim();
        try {
            if (!dni.matches("\\d{7,8}")) throw new IllegalArgumentException("DNI inválido (7 u 8 dígitos).");

            var res = ServiceRegistry.get().servicioAcceso.validarPorDni(dni);

            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
            String item = ts + " • DNI " + dni + " • " + (res.permitido() ? "PERMITIDO" : "DENEGADO")
                    + " (" + res.mensaje() + ")";

            ultimos.add(item);
            ultimosList.getItems().setAll(ultimos.toList());

            resultadoLabel.setText(res.permitido()
                    ? "Acceso permitido"
                    : "Acceso denegado: " + res.mensaje());

        } catch (Exception ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        } finally {
            dniField.requestFocus();
            dniField.selectAll();
        }
    }
}
