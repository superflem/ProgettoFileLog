package db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DbLoginTest {

    @Test
    void login() {
        UtentiDb utentiDb = new UtentiDb("utentidb");
        utentiDb.checkCreateDb();
        DbLogin dbl = new DbLogin();
        String email = "giulio.cesare@gmail.com";
        String password = "45687ce8916d17562c2c980f781bea4e5fd7c010801d1be688b87e28ccc8ab86e53b472a0cb7f1e6aa4cb2c28be6b7831998460e13ac6f464730057f9c3be307";
        int id = dbl.login(email,password,utentiDb);
        assertEquals(2,id);
    }
}