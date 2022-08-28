package data;
import java.io.*;
import java.nio.file.Files;
import java.util.InputMismatchException;
import java.util.stream.Collectors;

import application.ErrorPopup;
/**
 * Class which is a child of ClypeData, representing the file data sent between client and server 
 * 
 * @author Yijing
 *
 */
public class FileClypeData extends ClypeData implements FileClypeDataInterface {

    private File fileName;// Name of file
    private String fileContents;// Contents of file

    /**
     * Constructor to set up username, fileName,
     * and type
     * Set fileContents as null
     * 
     * @param userName  Name of client user
     * @param fileName  Name of file
     * @param type  The kind of data exchanged between the client and the server.
     * 				The variable type can take on the following values:
     *				0: give a listing of all users connected to this session
     *				1: log out, i.e., close this client's connection
     *				2: send a file
     *				3: send a message
     *              4: send a picture
     */
    public FileClypeData (String userName, File fileName,int type ) {
        super(userName,type);
        this.fileName = fileName;
        this.fileContents = "";

    }

    /**
     * Default constructor automatically create anonymous user with userName "Anon" and
     * type 2 (send a file) data
     * set fileName and fileContents as null
     */
    public FileClypeData() {
        super(ClypeData.SEND_A_FILE);
        this.fileName = null;
        this.fileContents = "";
    }

    /**
     * Mutator for the file name 
     * 
     * @param fileName  name of file
     */
    public void setFileName(File fileName ) {
        this.fileName = fileName;
    }

    /**
     * Accessor for file name
     * 
     * @return  the file name
     */
    public File getFileName() {
        return fileName;
    }

    /**
     * Accessor for file contents
     * 
     * @return file contents 
     */
    public String getData() {
        return fileContents;
    }

    /**
     * An override method to decrypt the string in `fileContents`
     * 
     * @param key	Decrypt the string using a key
     * @return	The decrypted string.
     */
    @Override
    public String getData(String key) {
        return super.decrypt(fileContents, key);
    }

    /**
     * An overloaded Mutator for fileContents and does secure file reads.
     * 
     * @param key	encrypt the contents to the instance variable `fileContents` using `key`
     * @throws IOException for IO error
     */
    public void readFileContents(String key) throws IOException{
        try {
            fileContents = Files.lines(fileName.toPath()).collect(Collectors.joining(System.lineSeparator()));
            fileContents = super.encrypt(fileContents, key); // Encrypt the fileContents
        }catch( FileNotFoundException fnfe ) {
            ErrorPopup errorPopup = new ErrorPopup(" Filename : " + fileName + " doesn't exist.");
            errorPopup.display();
        }catch( InputMismatchException ime ) {
            System.err.println("Did not enter string.");
        }catch( IOException ioe ) {
            System.err.println("IO error occurred.");
        }

    }

    /**
     * An overloaded Mutator for fileContents and does non-secure file reads.
     * 
     */
    public void readFileContents() throws IOException{
        try {
            fileContents = Files.lines(fileName.toPath()).collect(Collectors.joining(System.lineSeparator()));
        }catch( FileNotFoundException fnfe ) {
            System.err.println(" Filename : " + fileName + " doesn't exist.");
            // scan.close();
        }catch( InputMismatchException ime ) {
            System.err.println("Did not enter string.");
            // scan.close();
        }catch( IOException ioe ) {
            System.err.println("IO error occurred.");
            // scan.close();
        }
        
    }

    /**
     * An overloaded mutator for `fileContents` and does non-secure file writes. 
     */
    public void writeFileContents() {
        FileWriter writer = null;		
        try {
            writer = new FileWriter( fileName );
            writer.write(fileContents);
            writer.flush();
            writer.close();
        }catch( IOException ioe ) {
            System.err.println( "IO error occurred." );
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            System.err.println("Error in closing the writer.");
        }

    }

    /**
     * An overload mutator for `fileContents` and does secure file writes.
     * 
     * @param key	Decrypt the contents to the instance variable `fileContents` using `key`
     */
    public void writeFileContents(String key){
        FileWriter writer = null;		
        try {
            writer = new FileWriter( fileName );
            fileContents = super.decrypt(fileContents, key);// Decrypt the fileContents
            writer.write(fileContents);
            writer.flush();
            writer.close();
        }catch( IOException ioe ) {
            System.err.println( "IO error occurred." );
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            System.err.println("Error in closing the writer.");
        }
    }

    /**
     * hashCode() method overridden from
     * java.lang.Object to return a value of hash code for the object
     * 
     * @return  value of hash code
     */
    @Override
    public int hashCode() {
        int result = 17;
        String userName  = super.getUserName();
        int c = userName.hashCode();
        result = 37 * result + c;
        c = super.getDate().hashCode();
        result = 37 * result + c;
        c = super.getType();
        result = 37 * result + c;
        return result;
    }

    /**
     * equals() method overridden from java.lang.Object
     * 
     * @return  True if the two objects are equal when they are both FileClypeData with the same username, type, and date
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof FileClypeData)) return false;
        FileClypeData otherFile = (FileClypeData) other;
        return	super.getUserName().equals(otherFile.getUserName()) &&
        super.getType() == otherFile.getType() &&
        super.getDate().equals(otherFile.getDate());
    }

    /**
     * toString() method overridden from
     * java.lang.Object to return a description for FileClypeData
     * 
     * @return   full description for FileClypeData
     */
    @Override
    public String toString() {
        return "The type is : "+ 
        Integer.toString(super.getType()) + ". It is created on " + super.getDate() + 
        ", and this file is called \"" + fileName + "\" with contents :" + fileContents;

    }

}

