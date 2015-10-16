import java.io.*;

public class SimpleObjectWriter {

    public static SimpleObjectWriter openFileForWriting(String filename) {
        try {
            return new SimpleObjectWriter(new ObjectOutputStream(new FileOutputStream(filename)));
        }
        catch(IOException e) {
            return null;
        }
    }

    public void writeObject(Object o) {
        try {
            oos.writeObject(o);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            oos.writeObject(null);
            oos.close();
        }
        catch(IOException ioexception) { }
    }

    private SimpleObjectWriter(ObjectOutputStream oos) {
        this.oos = oos;
    }

    private ObjectOutputStream oos;
}