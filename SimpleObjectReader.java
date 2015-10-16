import java.io.*;

public class SimpleObjectReader {

    public static SimpleObjectReader openFileForReading(String filename) {
        try {
            return new SimpleObjectReader(new ObjectInputStream(new FileInputStream(filename)));
        }
        catch(IOException e) {
            return null;
        }
    }

    public Object readObject() {
        try {
            return ois.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            ois.close();
        }
        catch(IOException ioexception) { }
    }

    private SimpleObjectReader(ObjectInputStream ois) {
        this.ois = ois;
    }

    private ObjectInputStream ois;
}