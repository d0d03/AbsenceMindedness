package hr.absencemindedness.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {

    private static final String URL = "jdbc:h2:./absencemindedness;AUTO_SERVER=FALSE";
    private static Connection connection;

    private DatabaseManager(){
        throw new IllegalStateException("Utility class");
    }

    public static Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(URL, "sa", "");
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
        }
    }

    public static void close() throws SQLException{
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}
