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
 * Classe per la registrazione di un utente nel database
 */
public class Signup implements HttpHandler {
    private UtentiDb db;

    /**
     * Costruttore della signup
     * @param db
     */
    public Signup (UtentiDb db) {
        this.db = db;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        Integer rCode = 0;
        String response = "";

        String nome = "";
        String cognome = "";
        String email = "";
        String password = "";
        String professione = "";
        URI requestedUri = t.getRequestURI(); //prende l'uri contattato
        try {
            if ("POST".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/signup"))==0) { //se sono con il post in /signup
                //leggo il body (un oggetto json)
                InputStream input = t.getRequestBody();
                StringBuilder stringBuilder = new StringBuilder();

                new BufferedReader(new InputStreamReader(input))
                        .lines()
                        .forEach( (String s) -> stringBuilder.append(s + "\n") );

                // estraggo le informazioni dell'oggetto json
                String oggettoStringa = stringBuilder.toString();
                JSONObject oggettoJson = new JSONObject(oggettoStringa);

                nome = oggettoJson.getString("nome");
                cognome = oggettoJson.getString("cognome");
                email = oggettoJson.getString("email");
                password = oggettoJson.getString("password");
                professione = oggettoJson.getString("professione");

                if (db.signup(nome, cognome, email, password, professione)) { // eseguo la query

                    rCode = 200;
                    response = "{\"id\": \"registrato\"}";
                }
                else {

                    rCode = 200;
                    response = "{\"errore\": \"Mail gia' in uso\"}";
                }
            }
            else if ("OPTIONS".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/signup"))==0) {
                rCode = 200;
            }
            else {
                rCode = 404;
                response = "{\"errore\": \"Pagina non trovata\"}";
            }
        }
        catch (URISyntaxException e) { //errore nell'uri
           // e.printStackTrace();
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
