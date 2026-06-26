package hr.absencemindedness.repositories;

import hr.absencemindedness.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {

    private static final String URL = AppConfig.get("db.url");
    private static final String USER = AppConfig.get("db.user");
    private static final String FILE_PASSWORD = AppConfig.get("db.file.password");
    private static final String USER_PASSWORD = AppConfig.get("db.user.password");
    private static Connection connection;

    private DatabaseManager(){
        throw new IllegalStateException("Utility class");
    }

    public static Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(URL, USER,FILE_PASSWORD + " " + USER_PASSWORD );
        }
        return connection;
    }

    public static void initialize() throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS calendar_entries(
                    "year" INT,
                    "month" INT,
                    "day" INT,
                    "status" VARCHAR(50),
                    PRIMARY KEY ("year", "month","day")
                )
                """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS holidays(
                    "country_code" VARCHAR(2),
                    "year" INT,
                    "month" INT,
                    "day" INT,
                    "local_name" VARCHAR(255),
                    "name" VARCHAR(255),
                    PRIMARY KEY ("country_code", "year", "month", "day")
                )
                """);
        }
    }

    public static void close() throws SQLException{
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}
