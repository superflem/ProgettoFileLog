package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db.UtentiDb;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * Classe che permette all'admin di eliminare un utente dal db
 */
public class Elimina implements HttpHandler {
    private UtentiDb db;

    /**
     * Costruttore
     * @param db database
     */
    public Elimina (UtentiDb db) {
        this.db = db;
    }

    /**
     * Funziona che sta in ascolta su /elimina
     * @param t
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange t) throws IOException {
        String email = "";
        String response = "";
        Integer rCode = 0;
        URI requestedUri = t.getRequestURI(); //prende l'uri contattato
        try {
            if ("POST".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/elimina"))==0) { //se sono con il post in /elimina
                //leggo il body (un oggetto json)
                InputStream input = t.getRequestBody();
                StringBuilder stringBuilder = new StringBuilder();

                new BufferedReader(new InputStreamReader(input))
                        .lines()
                        .forEach( (String s) -> stringBuilder.append(s + "\n") );

                // estraggo le informazioni dell'oggetto json
                String oggettoStringa = stringBuilder.toString();
                JSONObject oggettoJson = new JSONObject(oggettoStringa);

                email = oggettoJson.getString("email");
                if (email.equals("pds.teamuno@gmail.com"))
                    response = "{\"errore\": \"Non puoi eliminare questo account\"}";
                else {
                    Integer errore = db.elimina(email);
                    if (errore==0)
                        response = "{\"errore\": \"Account non trovato\"}";
                    else
                        response = "{\"errore\": \"Account eliminato con successo\"}";
                }
                rCode = 200;
            }
            else if ("OPTIONS".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/elimina"))==0) { // per il preflight
                rCode = 200;
            }
            else { //diverso da post e options oppure usa un'altro url
                //System.out
                        //.println("URI non trovato");
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
