package dblog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe che fa le query al db dei log
 */
public class DblogQuery {
    /**
     * Funzione che esegue la query
     * @param db
     * @param testo
     * @param stato
     * @param from
     * @param to
     * @return
     * @throws SQLException
     */
    public String query (Dblog db, String testo, String stato, int from, int to) throws SQLException {
        Connection c = db.connect(); // si connette al db
        PreparedStatement pst = null; // prepara la query
        String oggettoRisposta = "";

        //queste variabili servono per vedere se devo filtrare per qualcosa o no
        boolean isStato = false;

        try {
            String queryLog = "select * from logfile where data>=? and data<=? "; //creo la query

            if (!stato.equals("")) { //se ho inserito uno stato, lo aggiungo nella query
                queryLog += "and paese=? ";
                isStato = true;
            }
            queryLog += "order by data;";

            //preparo la query
            pst = c.prepareStatement(queryLog);
            pst.setInt(1, from);
            pst.setInt(2, to);
            if (isStato)
                pst.setString(3, stato); //se ce solo lo stato

            pst.execute(); //eseguo la query

            ResultSet rs = pst.executeQuery(); //prendo il risultato

            oggettoRisposta = "\"log\": [{";
            boolean primaVolta = true; //serve per mettere la virgola bene

            //scorro i record del db
            while(rs.next()) {
                String tmp = ""; //prendo i risulti, poi eventualmente verifico che vadino bene
                if (!primaVolta)
                    tmp += ", {";

                tmp += "\"id\": \""+rs.getInt("id")+"\", "; //aggiungo l'id
                tmp += "\"request\": \""+rs.getString("request")+"\", "; //aggiungo la request
                tmp += "\"auth\": \""+rs.getString("auth")+"\", "; //aggiungo la auth
                tmp += "\"ident\": \""+rs.getString("ident")+"\", "; //aggiungo la ident
                tmp += "\"httpmethod\": \""+rs.getString("httpmethod")+"\", "; //aggiungo la request
                tmp += "\"time\": \""+rs.getString("time")+"\", "; //aggiungo la request
                tmp += "\"response\": \""+rs.getInt("response")+"\", "; //aggiungo la response
                tmp += "\"bytes\": \""+rs.getInt("bytes")+"\", "; //aggiungo i bytes
                tmp += "\"clientip\": \""+rs.getString("clientip")+"\", "; //aggiungo il clientip
                tmp += "\"rawrequest\": \""+rs.getString("rawrequest")+"\", "; //aggiungo la rawrequest
                tmp += "\"data\": \""+rs.getInt("data")+"\", "; //aggiungo la data
                tmp += "\"timestamp\": \""+rs.getString("timestamp")+"\", "; //aggiungo il timestamp
                tmp += "\"paese\": \""+rs.getString("paese")+"\""; //aggiungo la request
                tmp += "}";

                if ( (!testo.equals("") && tmp.contains(testo)) || testo.equals(""))  { //se ho filtrato del testo, controllo che nel record ci sia il testo che ho inserito
                    oggettoRisposta += tmp;
                    primaVolta = false;
                }
            }
            oggettoRisposta += "]";

            if (primaVolta) oggettoRisposta = "\"log\": []"; //se primaVOlta è true vuol dire che non ci sono record, quindi devo azzerare l'oggetto che restituisco
        }
        catch (SQLException ex) {
            //ex.printStackTrace();
        } finally {
            try {
                c.close(); //chiudo la connessione al db
            } catch (SQLException ex) {
                //ex.printStackTrace();
            }
        }
        return oggettoRisposta;
    }
}
