package dberrori;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;

/**
 * Classe per il database del log degli errori
 */
public class DblogErrori {
    private final String DBNAME;

    public DblogErrori(String dbname) {
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
            //c = getConnection("jdbc:sqlite:" + DBNAME);
        } catch (Exception e) {
            // eventuali errori
            System.exit(0);
        }
        return c;
    }

    /**
     * Crea la tabella utenti del db
     *
     * @param c connessione con db
     */
    private void createTableUser(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS logerror " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "giorno_della_settimana TEXT , " +
                    "mese TEXT," +
                    "giorno_del_mese INT," +
                    "orario TEXT," +
                    "anno INT," +
                    "data INT,"+
                    "tipo_errore TEXT," +
                    "pid INT, " +
                    "clientip TEXT," +
                    "porta_client INT ," +
                    "error_code TEXT, " +
                    "paese TEXT, " +
                    "payload TEXT)";

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Serve per ottenere il percorso assoluto della root del progetto
     *
     * @return il percorso
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

    public boolean insert(String giorno_della_settimana, String mese, int giorno_del_mese, String orario, int anno, long data, String tipo_errore,
                          int pid, String clientip, int porta_client, String error_code, String payload, String paese) throws SQLException {
        DbInsertError dbInsert = new DbInsertError();
        return dbInsert.insert(giorno_della_settimana, mese, giorno_del_mese, orario, anno, data, tipo_errore, pid, clientip, porta_client, error_code, payload, paese, this);
    }

    public String query (String testo, String stato, int from, int to) throws SQLException {
        DbErroriQuery query = new DbErroriQuery();
        return query.query(this, testo, stato, from, to);
    }
}


