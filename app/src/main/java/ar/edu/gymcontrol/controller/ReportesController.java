package ar.edu.gymcontrol.controller;

import ar.edu.gymcontrol.model.Socio;
import ar.edu.gymcontrol.service.ServiceRegistry;
import ar.edu.gymcontrol.service.ServicioReportes;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ReportesController {

    @FXML private TableView<Socio> tablaMorosos;
    @FXML private TableColumn<Socio, String> colDni, colApe, colNom, colCuota;
    @FXML private Label morososCount;

    @FXML private TextField dniBuscarField;
    @FXML private Label resultadoBusqueda;

    private final ServicioReportes service = ServiceRegistry.get().servicioReportes;

    @FXML
    public void initialize() {
        colDni.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getDni()));
        colApe.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getApellido()));
        colNom.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNombre()));
        colCuota.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(String.valueOf(c.getValue().getCuotaHasta())));
    }

    @FXML
    private void listarMorosos() {
        var data = service.morososOrdenados();
        tablaMorosos.getItems().setAll(data);
        morososCount.setText("Total: " + data.size());
    }

    @FXML
    private void buscarPorDni() {
        String dni = dniBuscarField.getText() == null ? "" : dniBuscarField.getText().trim();
        if (!dni.matches("\\d{7,8}")) {
            new Alert(Alert.AlertType.WARNING, "DNI inválido (7-8 dígitos)").showAndWait();
            return;
        }
        var res = service.buscarPorDniBinaria(dni);
        resultadoBusqueda.setText(res.map(Socio::toString).orElse("No encontrado"));
    }
}
