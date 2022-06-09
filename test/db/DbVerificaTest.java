package db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DbVerificaTest {

    @Test
    void verifica() {
        UtentiDb utentiDb = new UtentiDb("utentidb");
        utentiDb.checkCreateDb();
        DbVerifica dbv = new DbVerifica();
        String id = "2";
        try {
            assertEquals("Giulio Cesare",dbv.verifica(utentiDb,id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}