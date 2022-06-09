package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe che elimina un utente dal db
 */
public class DbElimina {

    /**
     * Funzione che elimina un utente data la email
     * @param db
     * @param email
     * @return
     * @throws SQLException
     */
    public Integer elimina(UtentiDb db, String email) throws SQLException  {
        Connection con = db.connect();

        try {
            String queryElimina = "delete from user where email=?";
            PreparedStatement pstmt = con.prepareStatement(queryElimina);
            pstmt.setString(1, email);
            return pstmt.executeUpdate();
        }
        catch (SQLException e) {
            //System.out
                    //.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                //System.out
                       // .println(e.toString());
            }
        }

        return 0;
    }
}
