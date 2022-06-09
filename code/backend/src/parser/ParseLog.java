package parser;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import dblog.Dblog;
import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.TimerTask;

/**
 * Classe per il parser dei log
 */
public class ParseLog extends TimerTask {
    /**
     * Converte il mese da stringa di tre lettere a numero a due cifre
     * @param mese il mese
     * @return il mese convertito
     */
    private String convertiMese(String mese){
        return switch (mese) {
            case "Jan" -> "01";
            case "Feb" -> "02";
            case "Mar" -> "03";
            case "Apr" -> "04";
            case "May" -> "05";
            case "Jun" -> "06";
            case "Jul" -> "07";
            case "Aug" -> "08";
            case "Sep" -> "09";
            case "Oct" -> "10";
            case "Nov" -> "11";
            case "Dec" -> "12";
            default -> "";
        };
    }
    @Override
    public void run() {
        // creo il parser dei file di log
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        grokCompiler.registerDefaultPatterns();

        //inserire pattern che deve compilare
        final Grok grok = grokCompiler.compile("%{IPORHOST:clientip} %{USER:ident} %{USER:auth} \\[%{HTTPDATE:timestamp}\\] \"(?:%{WORD:verb} %{NOTSPACE:request}(?: HTTP/%{NUMBER:httpversion})?|%{DATA:rawrequest})\" %{NUMBER:response} (?:%{NUMBER:bytes}|-)");

        //Classe per geolocalizzare indirizzo ip
        GeoIp geoip = new GeoIp();
        Azzera azzera = new Azzera();
        Dblog db = new Dblog("dblog");
        db.checkCreateDb();

        //leggo tutti i file della cartella
        File directoryPath = new File("." + File.separator + "code" + File.separator + "backend" + File.separator + "log_file");
       // System.out.println(directoryPath.getAbsolutePath());

        //li metto in un array e li leggo uno per uno
        File[] filesList = directoryPath.listFiles();

        Scanner sc;
        System.out.println("Inizio a parsare i file di log");
        for (File file : filesList) { //leggo ogni file una riga alla volta

            if (file.length() <= 0) //controlla se il file è vuoto e se lo è continua a leggere altrimenti lo parsa
                continue;

            if (file.getName().contains(".err")) {

                ErrorLogParser elp = new ErrorLogParser();
                try {
                    elp.parse(file, geoip);
                } catch (IOException | GeoIp2Exception | SQLException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }

            //stampa informazioni sul file
            System.out.println("File name: " + file.getName());

            //scanner del file
            try {
                sc = new Scanner(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            String input;

            //StringBuffer sb = new StringBuffer();
            while (sc.hasNextLine()) {
                input = sc.nextLine(); //legga la riga

                //fa il match della stringa in input con il pattern da matchare
                Match gm = grok.match(input);
                Map<String, Object> capture = gm.capture();


                String data = capture.get("YEAR").toString() + "-" + convertiMese(capture.get("MONTH").toString()) + "-" + capture.get("MONTHDAY").toString();

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date data_unix;
                try {
                    data_unix = df.parse(data);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                long unixTime = data_unix.getTime() /1000;

                String rawrequest;
                if (capture.get("rawrequest") == null)
                    rawrequest = "";
                else
                    rawrequest = capture.get("rawrequest").toString();

                try {
                    db.insert(capture.get("request").toString(),
                            capture.get("auth").toString(), capture.get("ident").toString(),
                            capture.get("verb").toString(), capture.get("TIME").toString(),
                            Integer.parseInt(capture.get("response").toString()),
                            Integer.parseInt(capture.get("bytes").toString()),
                            capture.get("clientip").toString(),
                            rawrequest, unixTime,
                            capture.get("timestamp").toString(),
                            geoip.getCountry(capture.get("clientip").toString()));
                } catch (SQLException | GeoIp2Exception | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.println("Ho eseguito l'inserimento dei dati nel db ");
        System.out.println("Inizio ad azzerare i file ");

        // Chiamo il metodo per azzerare i file che viene passato come parametro
        for (File file : filesList) {
            try {
                azzera.azzera(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Ho finito di azzerare i file ");
    }
}


