package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oggetti.OggettoVerifica;

public class DbVerifica {

    public OggettoVerifica verifica(UtentiDb db, String id) throws SQLException {
        Connection con = db.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        OggettoVerifica verifica = new OggettoVerifica("", "");
        try {
            String userNameSurname = "Select nome, cognome, professione from user where id=?"; // Per ottenere il nome di un utente
            ps = con.prepareStatement(userNameSurname);
            ps.setString(1, id);
            rs = ps.executeQuery();
            String nome = rs.getString(1);
            nome = upperCaseFirst(nome);
            String cognome = rs.getString(2);
            cognome = upperCaseFirst(cognome);

            verifica.nome = nome +" "+ cognome;
            verifica.professione = rs.getString(3);
        } catch (SQLException e) {
            //System.out
                    //.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
               // System.out
                        //.println(e.toString());
            }
        }
        return verifica;
    }

    private String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }
}
