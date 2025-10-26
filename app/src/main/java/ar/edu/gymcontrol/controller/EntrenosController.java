package ar.edu.gymcontrol.controller;

import ar.edu.gymcontrol.model.*;
import ar.edu.gymcontrol.service.ServiceRegistry;
import ar.edu.gymcontrol.service.ServicioEntrenamiento;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class EntrenosController {

    @FXML private TextField dniField;
    @FXML private DatePicker fechaPicker;
    @FXML private ComboBox<Ejercicio> ejercicioCombo;
    @FXML private TextField repsField, pesoField;
    @FXML private TableView<Serie> tablaSeries;
    @FXML private TableColumn<Serie, Number> colNro, colReps;
    @FXML private TableColumn<Serie, String> colEjercicio;
    @FXML private TableColumn<Serie, Number> colPeso, colVol;
    @FXML private Label volumenLabel, ultimaMarcaLabel, sesionInfo;

    private final ServicioEntrenamiento service = ServiceRegistry.get().servicioEntrenamiento;
    private SesionEntrenamiento sesionActual;

    @FXML
    public void initialize() {
        fechaPicker.setValue(LocalDate.now());
        ejercicioCombo.getItems().setAll(ServiceRegistry.get().ejercicioRepo.findAll());

        colNro.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNro()));
        colEjercicio.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getEjercicio().getNombre()));
        colReps.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getRepeticiones()));
        colPeso.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getPesoKg()));
        colVol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().volumen()));

        actualizarResumen();
    }

    @FXML
    private void abrirSesion() {
        String dni = dniField.getText() == null ? "" : dniField.getText().trim();
        if (!dni.matches("\\d{7,8}")) {
            new Alert(Alert.AlertType.WARNING, "DNI inválido").showAndWait(); return;
        }
        sesionActual = service.abrirSesion(dni, fechaPicker.getValue());
        sesionInfo.setText("Sesión: " + sesionActual.getId().substring(0, 6));
        refrescarTabla();
    }

    @FXML
    private void agregarSerie() {
        if (sesionActual == null) { new Alert(Alert.AlertType.WARNING,"Primero abrí una sesión").showAndWait(); return; }
        Ejercicio ej = ejercicioCombo.getValue();
        if (ej == null) { new Alert(Alert.AlertType.WARNING,"Elegí un ejercicio").showAndWait(); return; }
        try {
            int reps = Integer.parseInt(repsField.getText().trim());
            double peso = Double.parseDouble(pesoField.getText().trim());
            sesionActual = service.agregarSerie(sesionActual.getId(), ej.getId(), reps, peso);
            refrescarTabla();
            sesionActual.ultimaMarcaPorEjercicio(ej)
                    .ifPresent(s -> ultimaMarcaLabel.setText("Última marca ("+ej.getNombre()+"): "+s.getRepeticiones()+"x"+s.getPesoKg()+"kg"));
            repsField.clear(); pesoField.clear();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.WARNING, "Datos inválidos: " + ex.getMessage()).showAndWait();
        }
    }

    @FXML
    private void deshacer() {
        if (sesionActual == null) return;
        service.deshacerUltima(sesionActual.getId());
        refrescarTabla();
    }

    private void refrescarTabla() {
        tablaSeries.getItems().setAll(sesionActual.getSeries());
        actualizarResumen();
    }

    private void actualizarResumen() {
        double vol = (sesionActual == null) ? 0 : sesionActual.volumenTotal();
        volumenLabel.setText("Volumen total: " + vol + " kg");
    }
}
