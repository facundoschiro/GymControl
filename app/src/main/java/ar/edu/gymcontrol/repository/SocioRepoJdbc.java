package ar.edu.gymcontrol.repository;

import ar.edu.gymcontrol.model.EstadoSocio;
import ar.edu.gymcontrol.model.Socio;
import ar.edu.gymcontrol.persistence.ConnectionFactory;
import ar.edu.gymcontrol.persistence.DataAccessException;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class SocioRepoJdbc implements SocioRepository {
    private final ConnectionFactory cf = ConnectionFactory.get();

    @Override
    public Optional<Socio> findByDni(String dni) {
        String sql = """
      SELECT dni,
             nombre,
             COALESCE(apellido, '') AS apellido,
             estado,
             apto_medico_hasta AS apto_hasta,
             cuota_hasta
      FROM socio
      WHERE dni = ?
    """;
        try (Connection c = cf.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("findByDni", e);
        }
    }

    @Override
    public List<Socio> findAll() {
        String sql = """
      SELECT dni,
             nombre,
             COALESCE(apellido, '') AS apellido,
             estado,
             apto_medico_hasta AS apto_hasta,
             cuota_hasta
      FROM socio
    """;
        List<Socio> list = new ArrayList<>();
        try (Connection c = cf.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("findAll", e);
        }
    }

    @Override
    public void save(Socio s) {
        String update = """
    UPDATE socio
       SET nombre = ?, apellido = ?, estado = ?, apto_medico_hasta = ?, cuota_hasta = ?
     WHERE dni = ?
  """;
        try (Connection c = cf.getConnection(); PreparedStatement ps = c.prepareStatement(update)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getApellido());
            ps.setString(3, s.getEstado().name());
            ps.setDate  (4, Date.valueOf(s.getAptoHasta()));
            ps.setDate  (5, Date.valueOf(s.getCuotaHasta()));
            ps.setString(6, s.getDni());
            int n = ps.executeUpdate();

            if (n == 0) {
                String insert = """
        INSERT INTO socio (dni, nombre, apellido, estado, apto_medico_hasta, cuota_hasta, fecha_alta)
        VALUES (?, ?, ?, ?, ?, ?, ?)
      """;
                try (PreparedStatement ins = c.prepareStatement(insert)) {
                    ins.setString(1, s.getDni());
                    ins.setString(2, s.getNombre());
                    ins.setString(3, s.getApellido());
                    ins.setString(4, s.getEstado().name());
                    ins.setDate  (5, Date.valueOf(s.getAptoHasta()));
                    ins.setDate  (6, Date.valueOf(s.getCuotaHasta()));
                    ins.setDate  (7, Date.valueOf(java.time.LocalDate.now())); // << fecha_alta
                    ins.executeUpdate();
                }
            }
        } catch (SQLException e) {
            // mejor mensaje para depurar si vuelve a fallar
            throw new DataAccessException("save: " + e.getMessage(), e);
        }
    }


    @Override
    public void deleteByDni(String dni) {
        String sql = "DELETE FROM socio WHERE dni = ?";
        try (Connection c = cf.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("deleteByDni", e);
        }
    }

    private Socio map(ResultSet rs) throws SQLException {
        var s = new Socio(
                rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getDate("apto_hasta") == null ? java.time.LocalDate.now() : rs.getDate("apto_hasta").toLocalDate(),
                rs.getDate("cuota_hasta") == null ? java.time.LocalDate.now() : rs.getDate("cuota_hasta").toLocalDate()
        );
        s.setEstado(EstadoSocio.valueOf(rs.getString("estado")));
        return s;
    }
}
