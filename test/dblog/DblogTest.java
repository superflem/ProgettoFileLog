package dblog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DblogTest {
    @Test
    void getPercorso() {
        Dblog dblog = new Dblog("dblog");
        assertFalse(dblog.getPercorso().contains("backend"));
    }
}