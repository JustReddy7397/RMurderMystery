package ga.justreddy.wiki.rmurdermystery.builder;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class DatabaseBuilder {

    private Connection con;

    private int connections = 0;

    public void connectMysQL(String database, String username, String password, String host, int port) {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void connectH2(JavaPlugin plugin, String storagePath) {
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:" + plugin.getDataFolder().getAbsolutePath() + "/" + storagePath);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    public void closeConnection() {
        if (isConnected()) {
            try {
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean isConnected() {
        return con != null;
    }


    public void update(String qry) {
        try {
            getConnection().createStatement().executeUpdate(qry);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public ResultSet getResult(String qry) {
        try {
            return getConnection().createStatement().executeQuery(qry);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public PreparedStatement prepareStatement(String qry) {

        try{
            getConnection().prepareStatement(qry);
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return null;
    }


    public java.sql.Connection getConnection() {
        return con;
    }

}
