package ar.edu.gymcontrol.controller;

import ar.edu.gymcontrol.model.Ejercicio;
import ar.edu.gymcontrol.model.GrupoMuscular;
import ar.edu.gymcontrol.repository.EjercicioRepo;
import ar.edu.gymcontrol.service.ServiceRegistry;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EjerciciosController {

    @FXML private TableView<Ejercicio> tabla;
    @FXML private TableColumn<Ejercicio,String> colNombre, colGrupo;

    @FXML private TextField buscarField;
    @FXML private TextField nombreField;
    @FXML private ComboBox<GrupoMuscular> grupoCombo;

    private final EjercicioRepo repo = ServiceRegistry.get().ejercicioRepo;
    private Ejercicio seleccionado;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNombre()));
        colGrupo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getGrupo().name()));

        grupoCombo.getItems().setAll(GrupoMuscular.values());
        cargarTabla(repo.findAll());

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, e) -> {
            seleccionado = e;
            if (e != null) {
                nombreField.setText(e.getNombre());
                grupoCombo.setValue(e.getGrupo());
            } else {
                nombreField.clear();
                grupoCombo.setValue(null);
            }
        });
    }

    private void cargarTabla(List<Ejercicio> data) {
        data.sort(Comparator.comparing(Ejercicio::getNombre, String.CASE_INSENSITIVE_ORDER));
        tabla.getItems().setAll(data);
    }

    @FXML
    private void nuevo() {
        tabla.getSelectionModel().clearSelection();
        seleccionado = null;
        nombreField.clear();
        grupoCombo.setValue(null);
        nombreField.requestFocus();
    }

    @FXML
    private void guardar() {
        String nombre = (nombreField.getText() == null ? "" : nombreField.getText().trim());
        if (nombre.isEmpty()) { new Alert(Alert.AlertType.WARNING, "Nombre requerido").showAndWait(); return; }
        GrupoMuscular grupo = grupoCombo.getValue();
        if (grupo == null) { new Alert(Alert.AlertType.WARNING, "Elegí un grupo muscular").showAndWait(); return; }

        if (seleccionado == null) {
            Ejercicio e = new Ejercicio(nombre, grupo);
            repo.save(e);
        } else {
            seleccionado.setNombre(nombre);
            seleccionado.setGrupo(grupo);
            repo.save(seleccionado);
        }

        cargarTabla(repo.findAll());
        new Alert(Alert.AlertType.INFORMATION, "Guardado con éxito").showAndWait();
    }

    @FXML
    private void eliminar() {
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccioná un ejercicio").showAndWait();
            return;
        }
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar el ejercicio \"" + seleccionado.getNombre() + "\"?", ButtonType.OK, ButtonType.CANCEL);
        conf.setHeaderText("Confirmar eliminación");
        conf.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                repo.deleteById(seleccionado.getId());
                seleccionado = null;
                cargarTabla(repo.findAll());
                nombreField.clear();
                grupoCombo.setValue(null);
                new Alert(Alert.AlertType.INFORMATION, "Eliminado.").showAndWait();
            }
        });
    }

    @FXML
    private void buscar() {
        String q = buscarField.getText() == null ? "" : buscarField.getText().trim().toLowerCase();
        var filtrados = repo.findAll().stream()
                .filter(e -> e.getNombre().toLowerCase().contains(q))
                .collect(Collectors.toList());
        cargarTabla(filtrados);
    }

    @FXML
    private void limpiarBusqueda() {
        buscarField.clear();
        cargarTabla(repo.findAll());
    }
}
