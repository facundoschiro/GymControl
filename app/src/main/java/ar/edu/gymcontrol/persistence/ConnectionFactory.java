package ar.edu.gymcontrol.persistence;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public final class ConnectionFactory {
    private static ConnectionFactory INSTANCE;
    private final String url, user, pass;

    private ConnectionFactory() {
        try (InputStream in = getClass().getResourceAsStream("/db.properties")) {
            Properties p = new Properties(); p.load(in);
            url = p.getProperty("db.url"); user = p.getProperty("db.user"); pass = p.getProperty("db.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new DataAccessException("Config DB inválida", e);
        }
    }
    public static ConnectionFactory get() {
        return (INSTANCE == null) ? (INSTANCE = new ConnectionFactory()) : INSTANCE;
    }
    public Connection getConnection() {
        try { return DriverManager.getConnection(url, user, pass); }
        catch (SQLException e) { throw new DataAccessException("No se pudo conectar a MySQL", e); }
    }
}
