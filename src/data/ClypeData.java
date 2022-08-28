package data;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * An abstract class which represents the data sent between client and server
 * 
 * @author Yijing
 *
 */
public abstract class ClypeData implements ClypeDataInterface, Serializable {
    private  String userName; // Name of client user
    private  int type; // The type of data exchanged between the client and the server


    public static final int LIST_USERS = 0;
    public static final int LOG_OUT = 1;
    public static final int SEND_A_FILE = 2;
    public static final int SEND_A_MESSAGE = 3;
    public static final int SEND_A_PICTURE= 4;

    private  Date date; // Date when ClypeData object was created

    /**
     * Constructor to set up userName and type 
     * 
     * @param userName   name of client user
     * @param type  the kind of data exchanged between the client and the server.
     *              The variable type can take on the following values:
     *              0: give a listing of all users connected to this session
     *              1: log out, i.e., close this client's connection
     *              2: send a file
     *              3: send a message
     *              4: send a picture
     */
    public ClypeData(String userName, int type) {
        this.userName = userName;        
        this.type = type;
        date = new Date();
    }

    /**
     * Constructor to set up type and automatically create anonymous user with  
     * userName "Anon" 
     * 
     * @param type   the type of data exchanged between the client and the server
     */
    public ClypeData( int type) {
        this("Anon", type);
    }

    /**
     * Default constructor automatically creates anonymous user with userName "Anon" and
     * default type 3 : SEND_A_MESSAGE
     * 
     */
    public ClypeData() {
        this("Anon", SEND_A_MESSAGE);
    }

    /**
     * Accessor for type
     * 
     * @return  The type which represents the kind of data exchanged between the client and the server
     */
    public int getType() {
        return type;

    }

    /**
     * Accessor for userName
     * 
     * @return  The name of client user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Accessor for date
     * 
     * @return  The date when ClypeData object was created
     */
    public Date getDate() {
        return date;
    }

    /**
     * An overloaded abstract method for accessing data
     * 
     * @param key  the key used for descryption
     * @return  The original data in an encrypted string.
     */
    public abstract String getData(String key);

    /**
     * An overloaded abstract method for accessing data
     * 
     * @return  Data in String contained
     */
    public abstract String getData();

    /**
     * Method that implements the Vigenere cipher
     * to perform the encryption with a key provided as input
     * 
     * @param inputStringToEncrypt  An input string to be encrypted
     * @param key  Encrypt an input string using a key.
     *             It is recommended for the length of key as a prime number
     * @return The encrypted String
     */
    protected String encrypt( String inputStringToEncrypt, String key ) {
        String encryptedString = null;

        // Convert all input characters to bytes in  ASCII code
        byte[] inputStringInAscii;
        try {
            inputStringInAscii = inputStringToEncrypt.getBytes("US-ASCII");// Convert the input string into ASCII code            
            int inputStringLength = inputStringInAscii.length;

            byte[] keyInAscii = key.getBytes("US-ASCII");// Convert the key into ASCII code
            int keyLength = keyInAscii.length;

            /*
             * Encrypt the inputString
             */
            int k = 0;
            byte[] encryptedStringInAscii = new byte [ inputStringLength ];
            for (int i = 0 ; i < inputStringLength ; i++) {             
                int shift = 0;
                if( k < keyLength ){                    
                    if (65 <= keyInAscii[k] && keyInAscii[k] <= 90 ) {                    
                        shift = keyInAscii[k] - 65;                        
                    } else shift = keyInAscii[k] - 97;
                } else {
                    k=0;
                    if (65 <= keyInAscii[k] && keyInAscii[k] <= 90 ) {                    
                        shift = keyInAscii[k] - 65;                        
                    } else shift = keyInAscii[k] - 97;                    
                }
                if( 65 <= inputStringInAscii[i] && inputStringInAscii[i] <= 90) { // Range for upper-case letter in US-ASCII

                    if( inputStringInAscii[i] + shift <= 90 ) {                     
                        encryptedStringInAscii[i] = (byte) (inputStringInAscii[i] + shift);                     
                    } else encryptedStringInAscii[i] = (byte) (inputStringInAscii[i] + shift - 91 + 65);
                    k += 1 ;
                } else if( 97 <= inputStringInAscii[i] && inputStringInAscii[i] <= 122 ){  // Range for lower-case letter in US-ASCII

                    if( inputStringInAscii[i] + shift <= 122 ) {                        
                        encryptedStringInAscii[i] = (byte) (inputStringInAscii[i] + shift);                     
                    } else encryptedStringInAscii[i] = (byte) (inputStringInAscii[i] + shift - 123 + 97);
                    k += 1 ;
                } else {
                    encryptedStringInAscii[i] = inputStringInAscii[i];// Leave all non-alphabet unchanged, including space 
                }
            }

            encryptedString = new String (encryptedStringInAscii,"US-ASCII");// Convert encryptedinputString in US-ASCII code back into characters 

        }catch(UnsupportedEncodingException uee) {
            System.err.println("Cannot encode it in US-ASCII code.");
        }

        return encryptedString;
    }

    /**
     * Method that implements the backwards decryption of the Vigenere cipher
     * to perform the decryption with key provided as input
     * 
     * @param inputStringToDecrypt  An input string to be decrypted
     * @param key  Decrypt an input string using a key
     * @return  The decrypted string
     */
    protected String decrypt( String inputStringToDecrypt, String key ) {
        String decryptedString = null;

        byte[] inputStringInAscii;// Convert all input characters to bytes in  ASCII code
        try {
            inputStringInAscii = inputStringToDecrypt.getBytes("US-ASCII");

            int inputStringLength = inputStringInAscii.length;

            /* Create a new array to store the key in US-ASCII code repeated 
             * until the length of this array is as long as inputStringLength
             */
            byte[] keyInAscii = key.getBytes("US-ASCII");// Convert the key into US-ASCII code
            int keyLength = keyInAscii.length;

            /*
             * Decrypt the inputString
             */
            int k = 0;
            byte[] decryptedStringInAscii = new byte [ inputStringLength ];
            for (int i = 0 ; i < inputStringLength ; i++) {
                int shift = 0;
                if( k < keyLength ){                    
                    if (65 <= keyInAscii[k] && keyInAscii[k] <= 90 ) {                    
                        shift = keyInAscii[k] - 65;                        
                    } else shift = keyInAscii[k] - 97;
                } else {
                    k=0;
                    if (65 <= keyInAscii[k] && keyInAscii[k] <= 90 ) {                    
                        shift = keyInAscii[k] - 65;                        
                    } else shift = keyInAscii[k] - 97;                    
                }

                if( 65 <= inputStringInAscii[i] && inputStringInAscii[i] <= 90) {  // Range for upper-case letter in  ASCII

                    if( 65 <= inputStringInAscii[i] - shift) {                      
                        decryptedStringInAscii[i] = (byte) (inputStringInAscii[i] - shift);                     
                    } else decryptedStringInAscii[i] = (byte) ( 91 - (65 - inputStringInAscii[i] + shift) );
                    k += 1;
                } else if( 97 <= inputStringInAscii[i] && inputStringInAscii[i] <= 122 ){  // Range for lower-case letter in ASCII   

                    if( 97 <= inputStringInAscii[i] - shift ) {                     
                        decryptedStringInAscii[i] = (byte) (inputStringInAscii[i] - shift);                     
                    } else decryptedStringInAscii[i] = (byte) ( 123 - (97 - inputStringInAscii[i] + shift));// Leave all non-alphabet unchanged, including space
                    k += 1;
                } else {

                    decryptedStringInAscii[i] = inputStringInAscii[i];
                }
            }

            decryptedString = new String (decryptedStringInAscii,"US-ASCII"); //Convert decryptedinputString in US-ASCII code back into characters

        }catch(UnsupportedEncodingException uee) {
            System.err.println("Cannot encode it in US-ASCII code.");
        }

        return decryptedString;
    }

}
