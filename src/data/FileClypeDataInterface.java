package data;

import java.io.File;
import java.io.IOException;

public interface FileClypeDataInterface {
    // method signatures
    
    public void setFileName(File fileName );
    //implement the way to set fileName

    public File getFileName();
    //implement the way to get fileName

    public String getData();
    //implement the way to get data

    public void readFileContents(String key) throws IOException;
    //implement the way to read file contents and does secure file reads

    public void readFileContents() throws IOException;
    //implement the way to read file contents and does non-secure file reads

    public void writeFileContents();
    //implement the way to write file contents and does non-secure file writes.

    public void writeFileContents(String key);
    //implement the way to write file contents  and does secure file writes

    public int hashCode();
    //override the way to get hashcode

    public boolean equals(Object other);
    //override the way to check if two objects are equal

    public String toString();
    //override the way to get object description

    public String getData(String key);
    //override the way to decrypt the string in �fileContents� and return the decrypted string.

}
