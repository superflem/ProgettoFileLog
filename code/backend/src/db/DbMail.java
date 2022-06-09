package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe per gestire le mail per le notifiche
 */
public class DbMail {
    public ArrayList<String> indirizzi(UtentiDb db) throws SQLException {
        Connection con = db.connect();
        PreparedStatement ps;
        ResultSet rs;

        ArrayList<String> indirizziEmail = new ArrayList<>();

        try {
            String queryIndirizzi = "Select email from user where professione!=?"; // Per ottenere le mail di tecnici e admin
            ps = con.prepareStatement(queryIndirizzi);
            ps.setString(1, "cliente");
            rs = ps.executeQuery();
            while (rs.next()) {
                indirizziEmail.add(rs.getString("email"));
            }
        } catch (SQLException ex) {
            //ex.printStackTrace();
        } finally {
            try {
                con.close(); //chiudo la connessione al db
            } catch (SQLException ex) {
                //ex.printStackTrace();
            }
        }
        return indirizziEmail;
    }
}
