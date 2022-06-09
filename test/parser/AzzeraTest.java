package parser;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AzzeraTest {

    @Test
    void azzera() {
        File file = new File("."+File.separator+"test"+File.separator+"parser","fileTest");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("Prova dell'azzeramento\nPROVA");
            writer.close();
            Azzera az = new Azzera();
            az.azzera(file);
            assertEquals("", Files.readString(Path.of("."+File.separator+"test"+File.separator+"parser"+File.separator+"fileTest")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}