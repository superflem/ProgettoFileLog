package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import db.UtentiDb;
import oggetti.OggettoVerifica;

/**
 * Controlla i cookies per verificare che l'utente sia loggato
 */
public class Verifica implements HttpHandler {
    private UtentiDb db;

    public Verifica (UtentiDb db) {
        this.db = db;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = "";
        Integer rCode = 0;
        URI requestedUri = t.getRequestURI(); //prende l'uri contattato
        try {
            if ("POST".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/verifica"))==0) { //se sono con il post in /verifica
                String cookie = t.getRequestHeaders()
                        .getFirst("Cookie"); //prendo il cookie in formato chiave=valore
                if (cookie == null) {
                    response = "";
                }
                else {
                    String id = "-1";
                    int indice = cookie.indexOf(", id=");
                    if (indice != -1) //se l'id non è in prima posizione
                        id = cookie.substring(indice+5, indice+7);
                    else if (cookie.substring(0, 2).equals("id"))
                        id = cookie.substring(3, 5); //estraggo l'id del cookie che è in prima posizione

                    if (id.equals("-1")) {
                        response = "";
                    }
                    else {
                        OggettoVerifica oggetto = db.verifica(id);
                        String nome = oggetto.nome;
                        String professione = oggetto.professione;
                        response = "{\"nome\": \""+nome+"\", \"professione\":\""+professione+"\"}";
                    }
                }
                rCode = 200;
            }
            else if ("OPTIONS".equals(t.getRequestMethod()) && requestedUri.compareTo(new URI("/verifica"))==0) { //se è il preflight
                rCode = 200;
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
