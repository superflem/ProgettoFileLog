package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe per "cancellare" i file di log che vengono letti
 */
public class Azzera {
    /**
     * Cancella i file letti dal parser, sovrascrivendoli con una stringa vuota
     * @param file il file
     * @throws IOException eccezione I/O
     */
    public void azzera(File file) throws IOException {
        //Creo un buffer per scrivere dentro al file
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(""); //scrivo una stringa vuota così sovrascrivo il file
        writer.close(); //chiudo il file
    }
}
