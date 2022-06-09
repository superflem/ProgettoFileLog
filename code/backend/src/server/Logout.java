package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * Classe per il logout
 */
public class Logout implements HttpHandler {
    private Integer rCode = 0;
    private String response = "";

    @Override
    public void handle(HttpExchange t) throws IOException {
        URI requestedUri = t.getRequestURI(); //prende l'uri contattato

        rCode = 200;
        try {
            if ("POST".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/logout"))==0) { //se sono con il post in /logout
                response = "{\"id\": \"sloggato\"}";
                t.getResponseHeaders().set("Set-Cookie", "id=-1; HttpOnly; Expires=1");
            }
            else if ("OPTIONS".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/logout"))==0) {
                response = "{\"id\": \"sloggato\"}";
            }
            else {
                rCode = 404;
                response = "{\"errore\": \"Pagina non trovata\"}";
            }
        }
        catch (URISyntaxException e) { //errore nell'uri
            //e.printStackTrace();
            System.exit(1);
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
