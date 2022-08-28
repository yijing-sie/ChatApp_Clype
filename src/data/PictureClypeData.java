package data;

import java.io.File;

/**
 *Class which is a child of ClypeData, representing the picture file sent between client and server 
 * 
 * @author Yijing
 *
 */
public class PictureClypeData extends ClypeData implements PictureClypeInterface{

    private File picture; // Representing instant picture
    /**
     *Constructor to set up username, picture, key
     *and type
     * 
     * @param userName  Name of client user
     * @param picture  Picture for ClypeData
     * @param key	No use
     * @param type  The kind of data exchanged between the client and the server.
     * 				The variable type can take on the following values:
     *				0: give a listing of all users connected to this session
     *				1: log out, i.e., close this client's connection
     *				2: send a file
     *				3: send a message
     *              4: send a picture
     * 
     */
    public PictureClypeData( String userName, File picture, String key, int type ){
        super(userName, type);
        this.picture = picture;

    }
	
    /**
     *Constructor to set up username, picture, key
     *and type
     * 
     * @param userName  name of client user
     * @param picture  picture for ClypeData
     * @param type  the kind of data exchanged between the client and the server.
     * 				The variable type can take on the following values:
     *				0: give a listing of all users connected to this session
     *				1: log out, i.e., close this client's connection
     *				2: send a file
     *				3: send a message
     *              4: send a picture
     * 
     */
    public PictureClypeData( String userName, File picture, int type ){
        super(userName,type);
        this.picture = picture;

    }

    /**
     * Default constructor automatically create anonymous user with userName "Anon" 
     * with default type 4
     * 
     */
    public PictureClypeData() {
        super(4);
        this.picture = null;
    }

    /**
     * Accessor for the file picture
     * 
     * @return picture
     */
    public File getPicture() {
        return picture;
    }


    /**
     * Accessor for the picture name
     * 
     * @return picture's name
     */
    public String getData() {
        return picture.getName();
    }

    /**
     * Accessor for the picture name
     * 
     * @param key	no use
     * @return picture's name
     */
    @Override
    public String getData(String key) {
        return picture.getName();
    }

    /**
     * hashCode() method overridden from
     * java.lang.Object return a value of hash code for the object
     * 
     * @return  value of hash code
     */
    @Override
    public int hashCode() {
        int result = 17;
        String userName  = super.getUserName();
        int c = userName.hashCode();
        result = 37 * result + c;
        c = picture.hashCode();
        result = 37 * result + c;
        c = super.getType();		
        result = 37 * result + c;
        return result;
    }

    /**
     * 
     * equals() method overridden from java.lang.Object
     * 
     * @return  True if the two objects are equal when they are both PictureClypeData with the same userName, type, and Date
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PictureClypeData)) return false;
        PictureClypeData otherPicture = (PictureClypeData) other;

        return super.getUserName().equals(otherPicture.getUserName()) && 
        super.getType() == otherPicture.getType() &&				
        super.getDate().equals(otherPicture.getDate());
    }

    /**
     * toString() method overridden from
     * java.lang.Object return a description for MessageClypeData
     * 
     * @return   full description for MessageClypeData
     */
    @Override
    public String toString() {
        return "The user is called "+ super.getUserName()+", and the type is : "+ 
        Integer.toString(super.getType()) + ". It is created on " + super.getDate() + 
        ". The picture name is: " + getData(); 

    }

}
