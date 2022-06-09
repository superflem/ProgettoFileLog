package parser;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ErrorLogParserTest {

    @Test
    void parse() {
        File file = null;
        GeoIp geoIp = null;
        ErrorLogParser errorLogParser = new ErrorLogParser();
        Assertions.assertThrows(NullPointerException.class,()->{errorLogParser.parse(file,geoIp);});
    }
}