package dberrori;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe per la query nel db degli errori
 */
public class DbErroriQuery {
    /**
     * Funzione che fa la query sul db degli errori
     * @param db
     * @param testo
     * @param stato
     * @param from
     * @param to
     * @return
     * @throws SQLException
     */
    public String query (DblogErrori db, String testo, String stato, int from, int to) throws SQLException {
        Connection c = db.connect(); // si connette al db
        PreparedStatement pst = null; // prepara la query
        String oggettoRisposta = "";

        //queste variabili servono per vedere se devo filtrare per qualcosa o no
        boolean isStato = false;

        try {
            String queryLog = "select * from logerror where data>=? and data<=? "; //creo la query

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

            oggettoRisposta = "\"err\": [{";
            boolean primaVolta = true; //serve per mettere la virgola bene

            //scorro i record del db
            while(rs.next()) {
                String tmp = ""; //prendo i risulti, poi eventualmente verifico che vadino bene
                if (!primaVolta)
                    tmp += ", {";

                String timestamp = rs.getString("giorno_del_mese")+"/"+rs.getString("mese")+"/"; //creo il timestamp
                timestamp += rs.getInt("anno")+":"+rs.getString("orario");

                tmp += "\"id\": \""+rs.getInt("id")+"\", "; //aggiungo l'id
                tmp += "\"tipoErrore\": \""+rs.getString("tipo_errore")+"\", "; //aggiungo il tipo dell'errore
                tmp += "\"pid\": \""+rs.getInt("pid")+"\", "; //aggiungo il pid
                tmp += "\"clientip\": \""+rs.getString("clientip")+"\", "; //aggiungo il clientip
                tmp += "\"portaClient\": \""+rs.getInt("porta_client")+"\", "; //aggiungo il clientip
                tmp += "\"errorCode\": \""+rs.getString("error_code")+"\", "; //aggiungo la rawrequest
                tmp += "\"data\": \""+rs.getInt("data")+"\", "; //aggiungo la data
                tmp += "\"timestamp\": \""+timestamp+"\", "; //aggiungo il timestamp
                tmp += "\"paese\": \""+rs.getString("paese")+"\", "; //aggiungo la request
                tmp += "\"payload\": \""+rs.getString("payload").replaceAll("\"", "'")+"\""; //aggiungo la request (devo sostituire per evitare danni)
                tmp += "}";

                if ((!testo.equals("") && tmp.contains(testo)) || testo.equals(""))  { //se ho filtrato del testo, controllo che nel record ci sia il testo che ho inserito
                    oggettoRisposta += tmp;
                    primaVolta = false;
                }
            }
            oggettoRisposta += "]";
            if (primaVolta) oggettoRisposta = "\"err\":[]"; //se primaVOlta è true vuol dire che non ci sono record, quindi devo azzerare l'oggetto che restituisco
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                c.close(); //chiudo la connessione al db
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return oggettoRisposta;
    }
}
