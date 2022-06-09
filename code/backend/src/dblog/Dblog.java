package dblog;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;

/**
 * Classe per il database dei log
 */
public class Dblog {
    private final String DBNAME;

    public Dblog(String dbname) {
        this.DBNAME = dbname;
    }

    /**
     * Controlla di aver creato il Database e la tabella degli utenti.
     */
    public void checkCreateDb() {
        File file = new File(DBNAME);
        if (file.exists()) {
            System.out.println("Il database esiste");
        } else {
            try {
                //ottengo il percorso per il database
                String percorso = this.getPercorso();
                Class.forName("org.sqlite.JDBC");
                Connection c = getConnection(
                        "jdbc:sqlite:" + percorso + File.separator + "database" + File.separator +
                                DBNAME);
                // se non esiste, creo la tabella degli utenti
                createTableUser(c);
            } catch (Exception e) { // eventuali errori
                //System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    }

    /**
     * Si connette al Db
     *
     * @return Connection, la connessione al Db
     */
    public Connection connect() {
        Connection c = null;
        try {
            // ottiene il percorso al db
            String percorso = this.getPercorso();
            Class.forName("org.sqlite.JDBC");
            c = getConnection("jdbc:sqlite:" + percorso + File.separator + "database" + File.separator + DBNAME);

        } catch (Exception e) {
            // eventuali errori
            System.exit(0);
        }
        return c;
    }

    /**
     * Crea la tabella utenti del db
     *
     * @param c
     */
    private void createTableUser(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS logfile " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "request TEXT , " +
                    "auth TEXT," +
                    "ident TEXT," +
                    "httpmethod TEXT," +
                    "time TEXT," +
                    "response INT," +
                    "bytes INT, " +
                    "clientip TEXT," +
                    "rawrequest TEXT ," +
                    "data INT, " +
                    "timestamp TEXT , " +
                    "paese TEXT)";

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
           // System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Serve per ottenere il percorso assoluto della root del progetto
     *
     * @return
     */
    public String getPercorso() {
        File f = new File("");
        String percorso = f.getAbsolutePath();
        if (percorso.contains("/code/backend")) {
            percorso = percorso.replace("/code/backend", "/");
        } else if (percorso.contains("/code")) {
            percorso = percorso.replace("/code", "/");
        }
        return percorso;
    }

    public boolean insert(String request, String auth, String ident, String httpmethod, String time, int response,
                          int bytes, String clientip, String rawrequest, long data, String timestamp, String paese) throws SQLException {
        DbInsert dbInsert = new DbInsert();
        return dbInsert.insert(request, auth, ident, httpmethod, time, response, bytes, clientip, rawrequest, data, timestamp, paese, this);
    }

    public String query (String testo, String stato, int from, int to) throws SQLException {
        DblogQuery query = new DblogQuery();
        return query.query(this, testo, stato, from, to);
    }
}

