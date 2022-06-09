package parser;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GeoIpTest {

    @Test
    void getCountry() {
        String ip="101.56.0.0";
        GeoIp geoIp = new GeoIp();
        try {
            assertEquals("IT",geoIp.getCountry(ip));
        } catch (IOException | GeoIp2Exception e) {
            e.printStackTrace();
        }
    }
}