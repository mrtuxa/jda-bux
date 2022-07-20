package global.easycoding.database;

import global.easycoding.core.Main;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.sql.*;

public class DatabaseConnection {

    private static Dotenv dotenv = Main.dotenv;

    public enum DBType {MySQL, SQLite}

    /**
     * SQLite Data
     * Set this data if use DBType#SQLite
     * field location - this can either be relative or absolute path
     * ex: easycoding.db
     * or /home/mrtuxa/database/discord/easycoding.db
     */
    private final String location = "easycoding.db";

    private final String mysql_hostname = dotenv.get("MYSQL_HOSTNAME");
    private final String mysql_port = dotenv.get("MYSQL_PORT");
    private final String mysql_username = dotenv.get("MYSQL_USERNAME");
    private final String mysql_password = dotenv.get("MYSQL_PASSWORD");
    private final String mysql_database = dotenv.get("MYSQL_DATABASE");

    private Connection con = null;

    public DatabaseConnection(DBType dbType) {
        try {
            if (dbType == DBType.MySQL) {
                con = DriverManager.getConnection("jdbc:mysql:://" + mysql_hostname + "/" + mysql_database + "?autoReconnect=true", mysql_username, mysql_password);
                System.out.println("[DEBUG] Successfully initialized database connection");
            } else if (dbType == DBType.SQLite) {
                File database = new File(location);
                if (!database.exists()) {
                    System.out.println("[WARNING] SQLite file \"" + location + "\" not found, creating file...");
                    boolean create = database.createNewFile();
                    if (!create) System.out.println("[ERROR] Could not create SQLite file at" + location);
                }
                con = DriverManager.getConnection("jdbc:sqlite:" + location);
                System.out.println("[DEBUG] Successfully initialized database connection");
            }
        } catch (Exception exception) {
            System.out.println("[ERROR] " + exception.getMessage());
        }
    }

    public void disconnect() {
        try {
            con.clearWarnings();
            con.close();
            con = null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Connection getCon() {
        return con;
    }

    public ResultSet query(String sql, Object... preparedParamters) {
        try {
                PreparedStatement ps = con.prepareStatement(sql);
                int id = 1;
                for (Object preparedParameter : preparedParamters) {
                    ps.setObject(id, preparedParameter);
                    id++;
            }
                return ps.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public ResultSet query(String sql) {
        try {
            return con.prepareStatement(sql).executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void update(String sql, Object... preparedParameters) {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int id = 1;
            for (Object preparedParameter : preparedParameters) {
                ps.setObject(id, preparedParameter);
                id++;
            }
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String sql) {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean isConnceted() {
        return con != null;
    }
}
