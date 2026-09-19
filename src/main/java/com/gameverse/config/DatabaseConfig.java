package com.gameverse.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties properties = new Properties();
    private static final String databaseType;
    private static boolean sqliteInitialized = false;

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo db.properties en src/main/resources");
            }
            properties.load(input);
            databaseType = properties.getProperty("db.type", "sqlite").trim().toLowerCase();
            Class.forName(databaseType.equals("mysql")
                    ? properties.getProperty("db.mysql.driver", "com.mysql.cj.jdbc.Driver")
                    : "org.sqlite.JDBC");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la configuración de la base de datos", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (databaseType.equals("mysql")) {
            return DriverManager.getConnection(
                    properties.getProperty("db.mysql.url"),
                    properties.getProperty("db.mysql.user"),
                    properties.getProperty("db.mysql.password", "")
            );
        }

        Path databasePath = resolveSqlitePath();
        try {
            Files.createDirectories(databasePath.getParent());
        } catch (Exception e) {
            throw new SQLException("No se pudo crear la carpeta de la base local: " + databasePath.getParent(), e);
        }

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
        initializeSQLite(connection);
        return connection;
    }

    private static synchronized void initializeSQLite(Connection connection) throws SQLException {
        if (sqliteInitialized || !databaseType.equals("sqlite")) {
            return;
        }

        try (var statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS categorias ("
                    + "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT NOT NULL UNIQUE"
                    + ")");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS videojuegos ("
                    + "id_videojuego INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "titulo TEXT NOT NULL,"
                    + "descripcion TEXT NOT NULL,"
                    + "precio REAL NOT NULL CHECK (precio >= 0),"
                    + "stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),"
                    + "imagen_url TEXT,"
                    + "id_categoria INTEGER NOT NULL,"
                    + "FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)"
                    + ")");

            statement.executeUpdate("INSERT OR IGNORE INTO categorias (nombre) VALUES "
                    + "('Acción'), ('Aventura'), ('RPG'), ('Deportes'), ('Estrategia')");

            statement.executeUpdate("INSERT OR IGNORE INTO videojuegos "
                    + "(titulo, descripcion, precio, stock, imagen_url, id_categoria) "
                    + "SELECT 'The Legend of Zelda: Breath of the Wild', "
                    + "'Aventura de mundo abierto con exploración y desafíos.', 59.99, 10, NULL, id_categoria "
                    + "FROM categorias WHERE nombre = 'Aventura' "
                    + "AND NOT EXISTS (SELECT 1 FROM videojuegos "
                    + "WHERE titulo = 'The Legend of Zelda: Breath of the Wild')");

            statement.executeUpdate("INSERT OR IGNORE INTO videojuegos "
                    + "(titulo, descripcion, precio, stock, imagen_url, id_categoria) "
                    + "SELECT 'Minecraft', 'Construcción y supervivencia en un mundo abierto.', 29.99, 25, NULL, id_categoria "
                    + "FROM categorias WHERE nombre = 'Aventura' "
                    + "AND NOT EXISTS (SELECT 1 FROM videojuegos WHERE titulo = 'Minecraft')");

            statement.executeUpdate("INSERT OR IGNORE INTO videojuegos "
                    + "(titulo, descripcion, precio, stock, imagen_url, id_categoria) "
                    + "SELECT 'EA Sports FC 25', 'Simulador de fútbol con equipos y competiciones.', 69.99, 8, NULL, id_categoria "
                    + "FROM categorias WHERE nombre = 'Deportes' "
                    + "AND NOT EXISTS (SELECT 1 FROM videojuegos WHERE titulo = 'EA Sports FC 25')");

            statement.executeUpdate("INSERT OR IGNORE INTO videojuegos "
                    + "(titulo, descripcion, precio, stock, imagen_url, id_categoria) "
                    + "SELECT 'Baldur''s Gate 3', 'RPG de fantasía con decisiones y combates por turnos.', 49.99, 12, NULL, id_categoria "
                    + "FROM categorias WHERE nombre = 'RPG' "
                    + "AND NOT EXISTS (SELECT 1 FROM videojuegos WHERE titulo = 'Baldur''s Gate 3')");
        }

        sqliteInitialized = true;
    }

    private static Path resolveSqlitePath() {
        Path configuredPath = Paths.get(properties.getProperty("db.path", "database/gameverse.db"));
        if (configuredPath.isAbsolute()) {
            return configuredPath.normalize();
        }

        Path workingDirectory = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        Path workingDirectoryPath = workingDirectory.resolve(configuredPath).normalize();
        if (Files.isDirectory(workingDirectory.resolve("database"))) {
            return workingDirectoryPath;
        }

        try {
            Path codeLocation = Paths.get(DatabaseConfig.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()).toAbsolutePath().normalize();

            if (Files.isRegularFile(codeLocation)) {
                codeLocation = codeLocation.getParent();
            }

            Path candidate = codeLocation;
            while (candidate != null) {
                Path projectDatabaseDirectory = candidate.resolve("database");
                if (Files.isDirectory(projectDatabaseDirectory)) {
                    return projectDatabaseDirectory.resolve(configuredPath.getFileName()).normalize();
                }
                candidate = candidate.getParent();
            }
        } catch (Exception ignored) {
            // Se usa la ruta de trabajo como último recurso.
        }

        return workingDirectoryPath;
    }
}
