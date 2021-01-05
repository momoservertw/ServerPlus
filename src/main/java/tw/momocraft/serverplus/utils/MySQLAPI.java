package tw.momocraft.serverplus.utils;

import javafx.util.Pair;
import org.bukkit.entity.Player;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MySQLAPI {
    //DataBase vars.
    final String hostname = ConfigHandler.getConfig("config.yml").getString("MySQL-Utils.Settings.MySQL.hostname");
    final int port = ConfigHandler.getConfig("config.yml").getInt("MySQL-Utils.Settings.MySQL.port");
    final String username = ConfigHandler.getConfig("config.yml").getString("MySQL-Utils.Settings.MySQL.username");
    final String password = ConfigHandler.getConfig("config.yml").getString("MySQL-Utils.Settings.MySQL.password");
    final String database = ConfigHandler.getConfig("config.yml").getString("MySQL-Utils.Settings.MySQL.database");
    final String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database; //Enter URL w/db name

    //Connection vars
    static Connection connection;

    public MySQLAPI() {
        if (ConfigHandler.getConfig("config.yml").getBoolean("MySQL-Utils.Enable")) {
            setUp();
        }
    }

    // Connect the database.
    private void setUp() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            return;
        }
        try {
            connection = DriverManager.getConnection(url, username, password);
            CorePlusAPI.getLangManager().sendConsoleMsg(ConfigHandler.getPrefix(), "&fSucceed to connect the MySQL.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disabledConnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                CorePlusAPI.getLangManager().sendConsoleMsg(ConfigHandler.getPlugin(), "&fDisabled the MySQL connect.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////
    public void createTab(String sql) {
        // prepare the statement to be executed
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            // I use executeUpdate() to update the databases table.
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void search() throws SQLException {
        String sql = "SELECT * FROM myTable WHERE Something='Something'";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet results = stmt.executeQuery();
        if (!results.next()) {
            System.out.println("Failed");
        } else {
            System.out.println("Success");
        }
    }

    public void setValues(String value1, String value2, String value3) throws SQLException {
        PreparedStatement stat = connection.prepareStatement("INSERT INTO playerdata(PLAYER,VARIABLE,CONTENT) VALUES (\"" + value1 + "\",\"" + value2 + "\",\"" + value3 + "\");");
        stat.executeUpdate();
    }

    public void setScore(Player player, int score) throws SQLException {
        PreparedStatement stat = connection.prepareStatement("UPDATE PlayerScore SET Score = ? WHERE Player_Name = ?");
        stat.setString(2, player.getName());
        stat.setInt(1, score);
        stat.executeUpdate();
    }
    ///////////////////////////////////////////////////////////////////////////////////

    public Map<String, Pair<String, String>> getBankData() throws SQLException {
        PreparedStatement stat = connection.prepareStatement("SELECT uuid, money, exp FROM bankplayers");
        ResultSet result = stat.executeQuery();

        Map<String, Pair<String, String>> playerData = new HashMap<>();
        String uuid;
        String money;
        String exp;
        DecimalFormat df = new DecimalFormat("#");
        while (result.next()) {
            uuid = result.getString("uuid");
            money = df.format(result.getDouble("money"));
            exp = df.format(result.getDouble("exp"));
            if (money.equals("0") && exp.equals("0")) {
                continue;
            }
            playerData.put(uuid, new Pair<>(money, exp));
        }
        return playerData;
    }
}
