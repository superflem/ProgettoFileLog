package server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void main() throws IOException {
            String strUrl = "http://localhost:9000";

            try {
                URL url = new URL(strUrl);
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.connect();
                assertEquals(HttpURLConnection.HTTP_NOT_FOUND, urlConn.getResponseCode() );
            } catch (IOException e) {
                System.err.println("Error creating HTTP connection");
                e.printStackTrace();
                throw e;
            }
        }
    }