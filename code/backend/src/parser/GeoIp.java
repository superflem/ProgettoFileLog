package parser;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Classe per la geolocalizzazione dell'indirizzo ip
 */
public class GeoIp {

    /**
     * Ritorna la nazione dell'indirizzo ip
     * @param ip l'indirizzo ip
     * @return Stringa di due lettere che specifica la nazione dell'indirizzo ip
     * @throws IOException eccezione I/O
     * @throws GeoIp2Exception eccezione geolocalizzazione
     */
    public String getCountry(String ip) throws IOException, GeoIp2Exception {
        if (ip.equals("127.0.0.1")) //guardo che non sia in locale
            return "localhost";

        File database = new File("." + File.separator + "code" + File.separator + "backend" + File.separator + "lib" + File.separator + "GeoLite2-City.mmdb"); //legge il database per capire da dove vengono gli ip

        DatabaseReader reader = new DatabaseReader.Builder(database)
                .build(); //lettore del db (forse)

        InetAddress ipAddress = InetAddress.getByName(ip); //prendo l'indirizzo ip della classe giusta

        CityResponse response = reader.city(ipAddress); //cerco la città

        //restituisco lo stato
        Country country = response.getCountry();

        return country.getIsoCode();
    }
}