package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db.UtentiDb;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


import org.json.JSONObject;
import java.sql.SQLException;

/**
 * Classe per il login
 */
public class Login implements HttpHandler {

    private UtentiDb db;

    /**
     * Costruttore, riceve il db in input
     * @param db database degli utenti
     */
    public Login (UtentiDb db) {
        this.db = db;
    }

    /**
     * Metodo che viene invocato quando si contatta /login
     * @param t rappresente la connessione HTTP tra client e server
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange t) throws IOException {
        String email = "";
        String password = "";
        String response = "";
        int rCode = 0;

        URI requestedUri = t.getRequestURI(); //prende l'uri contattato
        try {
            if ("POST".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/login"))==0) {//se sono con il post in /login
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
                password = oggettoJson.getString("password");

                //calcolo l'eventuale id della persona loggata
                Integer id = db.login(email, password);

                if (id != -1) { // se il login è andato a buon fine setto il cookie e restituisco l'id
                    t.getResponseHeaders()
                            .set("Set-Cookie", "id=" + id + "; HttpOnly; Expires=900");
                    response = "{\"id\": \"pippo\"}";
                    rCode = 200;
                } else {
                    response = "{\"errore\": \"Email o password errati\"}";
                    rCode = 200;
                }
            }
            else if ("OPTIONS".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/login"))==0) { // per il preflight
                rCode = 200;
            }
            else { //diverso da post e options oppure usa un'altro url
               // System.out
                       // .println("URI non trovato");
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
