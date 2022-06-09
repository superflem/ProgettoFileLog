package db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DbSignupTest {

    @Test
    void signup() {
        UtentiDb utentiDb = new UtentiDb("utentidb");
        utentiDb.checkCreateDb();
        DbSignup dbs = new DbSignup();
        String nome = "Marco";
        String cognome = "Antonio";
        String email = "giulio.cesare@gmail.com";
        String password = "Sono pluto";
        String professione = "Tecnico";
        boolean funziona = false;
        try {
            funziona = dbs.signup(nome,cognome,email,password,professione,utentiDb);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            assertEquals(false,funziona);
        }
    }
}