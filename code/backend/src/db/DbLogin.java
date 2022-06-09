package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe che esegue la query del login sul db
 */
public class DbLogin {
    /**
     * Funzione che esegue la query del login sul db
     * @param email email
     * @param password password
     * @param db database
     * @return id ID della persona loggata
     */
    public int login(String email, String password, UtentiDb db) {
        Connection c = db.connect(); // si connette al db
        PreparedStatement pst = null; // prepara la query
        int id = -1;
        try {
            //preparo la query
            String sql = "Select id from user where email=? and password=?;";

            pst = c.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);
            pst.execute(); //eseguo la query

            ResultSet rs = pst.executeQuery(); //prendo il risultato
            id = rs.getInt("id");

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                c.close(); //chiudo la connessione al db
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return id;
    }
}
