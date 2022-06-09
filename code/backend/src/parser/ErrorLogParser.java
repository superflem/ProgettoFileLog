package parser;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import dberrori.DblogErrori;
import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Classe per il parsing dei log deli errori
 */
public class ErrorLogParser {

    /**
     * Converte il mese da parola in numero.
     *
     * @param mese il mese in parola
     * @return il mese in numero
     */
    private String convertiMese(String mese) {
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


    /**
     * Questa funzione realizza il parsing dei log degli errori, con geolocalizzazione ip.
     *
     * @param file  file dei log
     * @param geoip serve per la localizzazione
     * @throws IOException     eccezione lettura da file
     * @throws GeoIp2Exception eccezione localizzazione
     * @throws SQLException    eccezione database
     */
    public void parse(File file, GeoIp geoip) throws IOException, GeoIp2Exception, SQLException {
        DblogErrori db = new DblogErrori("dberr");
        db.checkCreateDb();

        Scanner sc = new Scanner(file);
        String input;
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        grokCompiler.registerDefaultPatterns();


        //inserire pattern che deve compilare
        final Grok grok = grokCompiler.compile("\\[%{DAY:giorno_della_settimana} %{MONTH:mese} %{MONTHDAY:giorno_del_mese} %{TIME:orario} %{YEAR:anno}\\] \\[:%{LOGLEVEL:tipo_errore}\\] \\[%{WORD:ignora} %{POSINT:pid}\\] \\[%{WORD:ignora} %{IP:clientip}:%{POSINT:porta_client}\\] \\[%{WORD:ignora} %{IP:ignora}\\] ModSecurity: %{WORD:errorcode}. %{GREEDYDATA:resto_del_mondo}");
        System.out.println("FILE degli errori");
        int c = 0;
        while (sc.hasNextLine()) {
            try {
                input = sc.nextLine(); //legga la riga
                Match gm = grok.match(input);
                Map<String, Object> capture = gm.capture();

                malevolo(capture);

                String data = capture.get("anno").toString() + "-" + convertiMese(capture.get("mese").toString()) +
                        "-" + capture.get("giorno_del_mese").toString();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dataUnix = df.parse(data);
                long unixTime = dataUnix.getTime() / 1000;
                c++;
                System.out.println("chiamo db insert " + c);
                db.insert(capture.get("giorno_della_settimana").toString(),
                        capture.get("mese")
                                .toString(), //ritora Aug, Sep ...
                        Integer.parseInt(capture
                                .get("giorno_del_mese")
                                .toString()),
                        capture.get("orario")
                                .toString(),
                        Integer.parseInt(capture
                                .get("anno")
                                .toString()),
                        unixTime,
                        capture.get("tipo_errore")
                                .toString(),
                        Integer.parseInt(capture
                                .get("pid")
                                .toString()),
                        capture.get("clientip")
                                .toString(),
                        Integer.parseInt(capture
                                .get("porta_client")
                                .toString()),
                        capture.get("errorcode")
                                .toString(),
                        geoip.getCountry(capture
                                .get("clientip")
                                .toString()),
                        capture.get("resto_del_mondo")
                                .toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private final HashMap<String, Controllo> ipSospetti = new HashMap<>(); //dizionario

    /**
     * Questa funzione serve per rilevare se c'è del traffico malevolo.
     *
     * @param capture risultato parsing con libreria grok
     * @throws ParseException errore parsing
     */
    private void malevolo(Map<String, Object> capture) throws ParseException, IOException, GeoIp2Exception {
        final int THRESHOLD = 2; //delta
        GeoIp ip = new GeoIp();

        String timestamp = capture.get("anno").toString() + "-" + convertiMese(capture.get("mese").toString()) +
                "-" + capture.get("giorno_del_mese").toString() + " " + capture.get("orario");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long currentTime = dateFormat.parse(timestamp).getTime() / 1000;

        ipSospetti.putIfAbsent(capture.get("clientip").toString(), new Controllo());

        ipSospetti.get(capture.get("clientip").toString()).check(currentTime, THRESHOLD, capture.get("clientip").toString()
                , ip.getCountry(capture.get("clientip").toString()));
    }
}