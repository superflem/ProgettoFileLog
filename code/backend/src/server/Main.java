package server;

import com.sun.net.httpserver.HttpServer;
import db.UtentiDb;
import parser.ParseLog;

import java.net.InetSocketAddress;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws Exception {
        // creo la connessione al db degli utenti
        UtentiDb dbUtenti = new UtentiDb("utentidb");
        dbUtenti.checkCreateDb();

        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0); //crea il server in ascolto sull porta 9000
        System.out.println("Server listening on port 9000");

        server.createContext("/login", new Login(dbUtenti)); //metodo per eseguire login con cookie se esiste utente nel db
        server.createContext("/signup", new Signup(dbUtenti)); //registrazione utente
        server.createContext("/verifica", new Verifica(dbUtenti)); //logged = true
        server.createContext("/logout", new Logout()); //Pagina di benvenuto
        server.createContext("/elimina", new Elimina(dbUtenti)); //pagina dell'admin per eliminare gli utenti registrati
        server.createContext("/query", new Query()); //pagina dell'admin per eliminare gli utenti registrati
        server.setExecutor(null); //crea un esecutore di default
        server.start(); //fa partire il server

        System.out.println("Eseguo il task assegnato");
        //creo uno scheduler che che esegue la classe passata con un rate fisso
        new Timer().scheduleAtFixedRate(new ParseLog(), 0, 100000); //100000 millis => 1.67 minuti
        System.out.println("Eseguito il task, ora aspetto");
        Thread.sleep(100000); //stesso commento di sopra
    }
}
