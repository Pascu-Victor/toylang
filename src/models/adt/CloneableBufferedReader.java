package models.adt;

import java.io.BufferedReader;
import java.io.IOException;

public class CloneableBufferedReader implements ICloneable {
    private final BufferedReader reader;
    
    public CloneableBufferedReader(BufferedReader reader) {
        this.reader = reader;
    }

    public CloneableBufferedReader deepCopy() {
        return new CloneableBufferedReader(this.reader);
    }

    public void close() throws IOException {
        reader.close();
    }

    public CloneableString readLine() throws IOException{
        return new CloneableString(reader.readLine());
    }
}
