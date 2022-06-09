package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dberrori.DblogErrori;
import dblog.Dblog;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * Classe per fare le query sul db dei log
 */
public class Query implements HttpHandler {

    /**
     * Funzione che viene eseguita quando si è in ascolto su /query
     * @param t comunicazione http
     * @throws IOException eccezzione io
     */
    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = "";
        int rCode = 0;
        String testo = "";
        String stato = "";
        String scegli = "";
        int from = 0;
        int to = 0;

        // creo le connessioni ai database
        DblogErrori dbErrori = new DblogErrori("dberr");
        Dblog dbLog = new Dblog("dblog");
        dbErrori.checkCreateDb();
        dbLog.checkCreateDb();

        URI requestedUri = t.getRequestURI(); //prende l'uri contattato
        try {
            if ("POST".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/query"))==0) {//se sono con il post in /login

                //leggo il body (un oggetto json)
                InputStream input = t.getRequestBody();
                StringBuilder stringBuilder = new StringBuilder();

                new BufferedReader(new InputStreamReader(input))
                        .lines()
                        .forEach( (String s) -> stringBuilder.append(s + "\n") );

                // estraggo le informazioni dell'oggetto json
                String oggettoStringa = stringBuilder.toString();
                JSONObject oggettoJson = new JSONObject(oggettoStringa);

                testo = oggettoJson.getString("testo");
                stato = oggettoJson.getString("stato");
                from = oggettoJson.getInt("from");
                to = oggettoJson.getInt("to");
                scegli = oggettoJson.getString("scegli");

                //creo la response (un oggetto json)
                response = "{";
                if (scegli.equals("buono")) {
                    response += dbLog.query(testo, stato, from, to);
                    response += ", \"err\":[]";
                }
                else if (scegli.equals("errore")) {
                    response += "\"log\":[], ";
                    response += dbErrori.query(testo, stato, from, to);
                }
                else {
                    response += dbLog.query(testo, stato, from, to);
                    response += ", ";
                    response += dbErrori.query(testo, stato, from, to);
                }

                response += "}";
            }
            else if ("OPTIONS".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/query"))==0) { // per il preflight
                rCode = 200;
            }
            else { //diverso da post e options oppure usa un'altro url
                rCode = 404;
                response = "{\"errore\": \"Pagina non trovata\"}";
            }
        }
        catch (URISyntaxException e) { //errore nell'uri
            //e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) { //errore db
            throw new RuntimeException(e);
        }
        catch (Exception e) { //errore nella lettura del body della request
            //e.printStackTrace();
        }

        //invio la risposta al client (gli header servono per le politiche di cors)
        String origine = t.getRequestHeaders()
                .get("Origin")
                .toString(); // l'origine serve per l'header sotto
        origine = origine.substring(1, origine.length()-1);
        t.getResponseHeaders()
                .add("Access-Control-Allow-Origin", origine);
        t.getResponseHeaders()
                .add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
        t.getResponseHeaders()
                .add("Access-Control-Allow-Credentials", "true");
        t.getResponseHeaders()
                .add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        t.getResponseHeaders()
                .add("Content-Type", "application/json"); //dico che la risposta sarà un json un json

        t.sendResponseHeaders(rCode, response.length());
        OutputStream os = t.getResponseBody(); //chiude la comunicazione
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
