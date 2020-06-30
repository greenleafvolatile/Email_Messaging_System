import java.io.*;

/**
 * I had a problem:
 * 1. I'd write multiple User objects to file.
 * 2. I'd attempt to read the User objects from the file.
 * 3. Only the User object that was written first would be successfully read, then I'd get a StreamCorruptedException.
 *
 * Apparently a file is expected to only have one serialization stream header (written by the ObjectOutputStream object).
 * Because I was writing multiple User object and creating a new ObjectOutputStream object each time, multiple headers would
 * be written which caused the StreamCorruptedException.
 *
 * AppendingObjectOutputStream, which extends ObjectOutputStream, is the solution.
 * This is not my code. I found it here:
 * https://stackoverflow.com/questions/1194656/appending-to-an-objectoutputstream
 * Props to Andreas Dolk!
 */

public class AppendingObjectOutputStream extends ObjectOutputStream {

    public AppendingObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        reset();
    }

}