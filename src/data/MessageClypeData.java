package data;

/**
 *Class which is a child of ClypeData, representing the message data sent between client and server 
 * 
 * @author Yijing
 *
 */
public class MessageClypeData extends ClypeData implements MessageClypeInterface{

    private String message; // representing instant message
    /**
     *Constructor to set up username, message, key
     *and type
     * 
     * @param userName  Name of client user
     * @param message  Instant message
     * @param key	Encrypt the message using the key
     * @param type  The kind of data exchanged between the client and the server.
     * 				The variable type can take on the following values:
     *				0: give a listing of all users connected to this session
     *				1: log out, i.e., close this client's connection
     *				2: send a file
     *				3: send a message
     * 
     */
    public MessageClypeData( String userName, String message, String key, int type ){
        super(userName,type);
        this.message = super.encrypt(message, key); // encrypt the message by key

    }
	
    /**
     *Constructor to set up username, message,
     *and type
     * 
     * @param userName  Name of client user
     * @param message  Instant message
     * @param type  The kind of data exchanged between the client and the server.
     * 				The variable type can take on the following values:
     *				0: give a listing of all users connected to this session
     *				1: log out, i.e., close this client's connection
     *				2: send a file
     *				3: send a message
     *              4: send a picture
     */
    public MessageClypeData(String userName, String message, int type) {
        super(userName,type);
        this.message = message;
    }

    /**
     * Default constructor automatically create anonymous user with userName "Anon" 
     * with default type 3
     * 
     */
    public MessageClypeData() {
        super();
        this.message = null;
    }

    /**
     * Accessor for message
     * 
     * @return  the data contained in this class
     */
    public String getData() {
        return message;
    }

    /**
     * An override method to decrypt the string in `message`
     * 
     * @param key	Decrypt an input string using a key
     * @return	The decrypted string
     */
    @Override
    public String getData(String key) {
        return super.decrypt(message, key);// Decrypt the message by key
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
     * 
     * equals() method overridden from java.lang.Object
     * 
     * @return  True if the two objects are equal when they are both MessageClypeData with the same username, type, and date
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MessageClypeData)) return false;
        MessageClypeData otherMessage = (MessageClypeData) other;

        return super.getUserName().equals(otherMessage.getUserName()) &&
        super.getType() == otherMessage.getType() &&				
        super.getDate().equals(otherMessage.getDate());
    }

    /**
     * toString() method overridden from
     * java.lang.Object with a description for MessageClypeData
     * 
     * @return   full description for MessageClypeData
     */
    @Override
    public String toString() {
        return "The user is called "+ super.getUserName()+", and the type is : "+ 
        Integer.toString(super.getType()) + ". It is created on " + super.getDate() + 
        ". The message is: " + message; 

    }

	

}
