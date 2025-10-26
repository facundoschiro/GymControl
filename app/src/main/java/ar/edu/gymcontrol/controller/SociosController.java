package ar.edu.gymcontrol.controller;

import ar.edu.gymcontrol.model.EstadoSocio;
import ar.edu.gymcontrol.model.Socio;
import ar.edu.gymcontrol.repository.SocioRepo;
import ar.edu.gymcontrol.service.ServiceRegistry;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SociosController {

    @FXML private TableView<Socio> tabla;
    @FXML private TableColumn<Socio,String> colDni, colApe, colNom, colEstado, colApto, colCuota;

    @FXML private TextField buscarField;

    @FXML private TextField dniField, apeField, nomField;
    @FXML private ComboBox<EstadoSocio> estadoCombo;
    @FXML private DatePicker aptoPicker, cuotaPicker;

    private final SocioRepo repo = ServiceRegistry.get().socioRepo;

    @FXML
    public void initialize() {
        colDni.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getDni()));
        colApe.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getApellido()));
        colNom.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNombre()));
        colEstado.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getEstado().name()));
        colApto.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(String.valueOf(c.getValue().getAptoHasta())));
        colCuota.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(String.valueOf(c.getValue().getCuotaHasta())));

        estadoCombo.getItems().setAll(EstadoSocio.values());
        cargarTabla(repo.findAll().stream().collect(Collectors.toList()));

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, s) -> {
            if (s != null) cargarFormulario(s);
        });
    }

    private void cargarTabla(List<Socio> data) {
        data.sort(Comparator.comparing(Socio::getApellido).thenComparing(Socio::getNombre));
        tabla.getItems().setAll(data);
    }

    private void cargarFormulario(Socio s) {
        dniField.setText(s.getDni());
        apeField.setText(s.getApellido());
        nomField.setText(s.getNombre());
        estadoCombo.setValue(s.getEstado());
        aptoPicker.setValue(s.getAptoHasta());
        cuotaPicker.setValue(s.getCuotaHasta());
        dniField.setDisable(true); // clave no editable
    }

    @FXML
    private void nuevo() {
        tabla.getSelectionModel().clearSelection();
        dniField.clear(); apeField.clear(); nomField.clear();
        estadoCombo.setValue(EstadoSocio.ACTIVO);
        aptoPicker.setValue(LocalDate.now().plusMonths(6));
        cuotaPicker.setValue(LocalDate.now().plusDays(30));
        dniField.setDisable(false);
        dniField.requestFocus();
    }

    @FXML
    private void guardar() {
        try {
            String dni = validarDni(dniField.getText());
            String ape = validarTexto(apeField.getText(), "Apellido");
            String nom = validarTexto(nomField.getText(), "Nombre");
            EstadoSocio est = estadoCombo.getValue() == null ? EstadoSocio.ACTIVO : estadoCombo.getValue();
            LocalDate apto = aptoPicker.getValue() == null ? LocalDate.now().plusMonths(6) : aptoPicker.getValue();
            LocalDate cuota = cuotaPicker.getValue() == null ? LocalDate.now().plusDays(30) : cuotaPicker.getValue();

            Socio existente = repo.findByDni(dni).orElse(null);
            if (existente == null) {
                Socio s = new Socio(dni, nom, ape, apto, cuota);
                s.setEstado(est);
                repo.save(s);
            } else {
                existente.setApellido(ape);
                existente.setNombre(nom);
                existente.setEstado(est);
                existente.setAptoHasta(apto);
                existente.setCuotaHasta(cuota);
                repo.save(existente);
            }

            recargar();
            new Alert(Alert.AlertType.INFORMATION, "Guardado con éxito").showAndWait();
            dniField.setDisable(true);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        }
    }

    @FXML
    private void eliminar() {
        Socio sel = tabla.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccioná un socio").showAndWait();
            return;
        }
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar al socio " + sel + "?", ButtonType.OK, ButtonType.CANCEL);
        conf.setHeaderText("Confirmar eliminación");
        conf.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                repo.deleteByDni(sel.getDni());   // <-- delete real
                recargar();
                limpiarFormulario();
                new Alert(Alert.AlertType.INFORMATION, "Eliminado.").showAndWait();
            }
        });
    }

    @FXML
    private void buscar() {
        String q = buscarField.getText() == null ? "" : buscarField.getText().trim().toLowerCase();
        var filtrados = repo.findAll().stream()
                .filter(s -> s.getDni().contains(q) || s.getApellido().toLowerCase().contains(q))
                .collect(Collectors.toList());
        cargarTabla(filtrados);
    }

    @FXML
    private void limpiarBusqueda() {
        buscarField.clear();
        recargar();
    }

    private void recargar() {
        cargarTabla(repo.findAll().stream().collect(Collectors.toList()));
    }
    private void limpiarFormulario() {
        tabla.getSelectionModel().clearSelection();
        dniField.clear(); apeField.clear(); nomField.clear();
        estadoCombo.setValue(null);
        aptoPicker.setValue(null);
        cuotaPicker.setValue(null);
        dniField.setDisable(false);
    }

    // --- Validaciones ---
    private String validarDni(String raw) {
        String v = raw == null ? "" : raw.trim();
        if (!v.matches("\\d{7,8}")) throw new IllegalArgumentException("DNI inválido (7-8 dígitos).");
        return v;
    }
    private String validarTexto(String raw, String campo) {
        String v = raw == null ? "" : raw.trim();
        if (v.isEmpty()) throw new IllegalArgumentException(campo + " requerido.");
        return v;
    }
}
