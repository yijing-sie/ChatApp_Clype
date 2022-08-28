
package main;

import data.ClypeData;
import data.FileClypeData;
import data.MessageClypeData;
import data.PictureClypeData;

import javafx.util.Pair;
import application.HostNotFoundPopup;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class which represents the client user, containing the user name,
 * host name of the server connected to, port number connected to, and a boolean `closeConnection` designating
 * whether the connection is open or not
 * 
 * @author Yijing
 *
 */
public class ClypeClient implements ClypeClientInterface{
    private String userName;// Name of the client
    private String hostName;// Name of the computer representing the server
    int port;// Integer which represents the port number server connected to
    public volatile boolean closeConnection;// Boolean which represents whether the connection is closed or not
    ClypeData dataToSendToServer;// ClypeData object which represents data sent to the server
    ClypeData dataToReceiveFromServer;// ClypeData object which represents data received from the server
    private static final String KEY = "ROYAL"; // Key stored as a constant which is used for encryption and decrytion 
    private ObjectOutputStream outToServer;// Send data packets from server using ObjectOutputStream.
    private ObjectInputStream inFromServer;// Receive data packets from server using ObjectInputStream.
    private Object objectData; // Object read from the server's data packets
    private Socket server = null; 
    /**
     * Constructor for username, host name, and port with default closeConnection false 
     * 
     * @param userName  Name of the client
     * @param hostName  Name of the computer representing the server
     * @param port  Integer which represents port number server connected to
     * @throws IllegalArgumentException Exception thrown when port number is less than 1024
     */
    public ClypeClient(String userName, String hostName, int port ) throws IllegalArgumentException {
        if( userName == null) throw new IllegalArgumentException("Username is null.");
        else this.userName = userName;
        if( hostName == null) throw new IllegalArgumentException("Hostname is null.");
        else this.hostName = hostName;
        if( port < 1024 ) throw new IllegalArgumentException("port shouldn't be lesser than 1024.");
        else this.port = port;

        closeConnection = false;
        dataToSendToServer = null;
        dataToReceiveFromServer = null;
        outToServer = null;
        inFromServer = null;
        objectData = null;
    }

    /**
     * Constructor for username, host name with default `closeConnection` set to false and default `port` set to 7000
     * 
     * @param userName  name of the client
     * @param hostName  name of the computer representing the server
     */
    public ClypeClient(String userName, String hostName) {
        this(userName, hostName, 7000);
    }

    /**
     * Constructor for username with default `closeConnection` set to false, default `port` set to 7000, and default 
     * `hostName` set to "localhost"
     * 
     * @param userName  name of the client
     */
    public ClypeClient(String userName) {
        this(userName, "localhost", 7000);

    }

    /**
     * Default constructor as `closeConnection` set to false, `port` set to 7000, and 
     * `hostName` set to "localhost"
     */
    public ClypeClient() {
        this("Anon", "localhost", 7000);
    }	

    /**
     * Start this client's communication with the server
     */
    public boolean start() {
        try {
            server = new Socket(hostName, port); // Connect to the server with the hostName and port number
            outToServer = new ObjectOutputStream(server.getOutputStream());
            inFromServer = new ObjectInputStream(server.getInputStream());
            dataToSendToServer = new MessageClypeData(this.userName,"This is user name", ClypeData.SEND_A_MESSAGE); // Send a message to the server to update the friendlist inside the chat room
            sendData();
            return false; // Connection succeeded
        } catch (UnknownHostException uhe) {
            System.err.println("UnknownHostException" + uhe.getMessage());
        } catch (IOException ioe) {            
            if(ioe.getMessage().equals("Connection refused: connect")) {
            	HostNotFoundPopup hnfp = new HostNotFoundPopup(); // A popup window warning the user that the server is not found
            	hnfp.display();
                try {
                    outToServer.close();
                    inFromServer.close();
                    server.close();            
                }catch(Exception ignore) {}
            } else {
            	System.err.println("IOException occurred:" + ioe.getMessage());
            }
        }

       return true; // Connection failed

   }
           
    /**
     * Close the client's connection to the server
     */
    public void closeClientConnection() {
        closeConnection = true;
        ClypeData leaveClype = new MessageClypeData(this.userName, "LEAVE", ClypeData.LOG_OUT); // Send a message to the server that the client is logging out
        try {
            outToServer.writeObject(leaveClype);
            outToServer.flush();
        } catch (IOException e) {						
            System.err.println("IOException occurred: " + e.getMessage());
        } 

        try {
            outToServer.close();
            inFromServer.close();
            server.close();              
        }catch(IOException ioe) {
            System.err.println("IOException occerred while closing");                   
        }

   }

    /**
     * Wrap the client's messages as a data packet 
     */
    public void readClientMessage(String message) {
        dataToSendToServer = null;    	
        try {
            dataToSendToServer = new MessageClypeData(this.userName, message, ClypeData.SEND_A_MESSAGE);
        }catch(Exception e) {
            e.getMessage();
        }
    }

    /**
     * Send the message to the server
     */
    public void sendData() {
        if (dataToSendToServer != null) {
            try {
                outToServer.writeObject(dataToSendToServer);
                outToServer.flush();
            } catch (IOException e) {						
                System.err.println("IOException occurred: " + e.getMessage());
            } 
        }
    }

    /**
     * Send the picture file to the server
     */
    public void sendPictureToServer(File file) {
        if (!closeConnection) {
            try {
                ClypeData ClypePicture = new PictureClypeData(this.userName, file, ClypeData.SEND_A_PICTURE);
                outToServer.writeObject(ClypePicture);
                outToServer.flush();
            } catch (IOException e) {						
                System.err.println("IOException occurred: " + e.getMessage());
            }
        }	
    }

    /**
     * Encrypt the text file and send it to the server
     */
    public void sendFileToServer(File fileName) {
        if (!closeConnection) {
            FileClypeData ClypeFile = new FileClypeData(this.userName, fileName, ClypeData.SEND_A_FILE);
            try {
                ClypeFile.readFileContents(KEY); // Encrypt the text file
                outToServer.writeObject(ClypeFile);
                outToServer.flush();
            }          
            catch (IOException ioe) {
                System.err.println( "IO error occurred." );
            }                                
        }
    }

    /**
     * Accessor for the data sent from the server
     */
    public void receiveData() {
        dataToReceiveFromServer = null;

        if (!closeConnection) {            
            try {
                objectData = inFromServer.readObject();
                dataToReceiveFromServer = (ClypeData) objectData; // Data received from server                
            } catch (IOException ex) {
                // Host closed connection exception
                closeConnection = true;
                try {
                    outToServer.close();
                    inFromServer.close();
                    server.close();              
                }catch(IOException ioe) {
                    System.err.println("IOException occerred while closing");                   
                }
            } catch (ClassNotFoundException cnfe) {
                System.err.println("Class not locatable.");
            } catch(ClassCastException c) {
                System.err.println("Class not castable.");
            }
        }
    }

    /**
     * Send the message from `dataToReceiveFromServer` to the application
     * @return a Pain<boolean, String> with key representing if the message is for updating the friendlist
     * and the value representing the content of the message
     * 
     * The key is true if the message is for updating the friendlist
     */
    public Pair<Boolean, String> printData() {       	    	
        if (dataToReceiveFromServer != null && !closeConnection) {
            if (dataToReceiveFromServer.getType() == ClypeData.SEND_A_MESSAGE) {
            	String message = dataToReceiveFromServer.getUserName() + " : " + dataToReceiveFromServer.getData();
                return new Pair<Boolean, String>(false, message);
            } 
            else if (dataToReceiveFromServer.getType() == ClypeData.LIST_USERS) {
                String message = dataToReceiveFromServer.getData(); 
                return new Pair<Boolean, String>(true, message);        		
            }          
        }
        
        return null;
    }

    /**
     * Decrypt the text file and convert the file contents from `dataToReceiveFromServer` into String
     * @return a Pare<String, String> with the key representing the text and the value representing the username that this text file is from
     */   
    public Pair<String, String> printText() {
        if (dataToReceiveFromServer != null && !closeConnection){
            if (dataToReceiveFromServer.getType() == ClypeData.SEND_A_FILE) {
                String text = dataToReceiveFromServer.getData(KEY);
                return new Pair<String,String>(text, dataToReceiveFromServer.getUserName());
            }
        }
        return null; 
    }
    
    /**
     * Send the picture file receiveed from the server to the applicaton
     * @return a Pare<File, String> with the key representing the picture file and the value representing the username that this picture file is from
     */
    public Pair<File, String> printPicture() {   
        if ( dataToReceiveFromServer != null && !closeConnection ) {
            if (dataToReceiveFromServer.getType() == ClypeData.SEND_A_PICTURE) {
                PictureClypeData dataToReceiveFromServer = (PictureClypeData) this.dataToReceiveFromServer;
                return new Pair<File, String>(dataToReceiveFromServer.getPicture(), dataToReceiveFromServer.getUserName());
            } 
        }
    	return null;
    }

    /**
     * Accessor for the userName
     * 
     * @return	the user name of the client
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Accessor for the hostName
     * 
     * @return	name of the computer representing the server
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Accessor for the port
     * 
     * @return	integer which represents the port number server connected to
     */
    public int getPort() {
        return port;
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
        int c = userName.hashCode();
        result = 37 * result + c;
        c = hostName.hashCode();
        result = 37 * result + c;		
        c = port;
        result = 37 * result + c;
        c = closeConnection ? 1:0;	
        result = 37 * result + c;
        return result;
    }

    /**
     * equals() method overridden from java.lang.Object
     * 
     * @return  True if the two objects are both ClypeClient with the same username, host name, port number, and connection status
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ClypeClient)) return false;
        // if (other == null ) return false;
        ClypeClient otherClient = (ClypeClient) other;
        return this.userName.equals(otherClient.userName) &&
        this.hostName.equals(otherClient.hostName) &&
        this.port == otherClient.port &&
        this.closeConnection == otherClient.closeConnection;
    }

    /**
     * toString() method overridden from
     * java.lang.Object to return a description for ClypeClient
     * 
     * @return   full description for ClypeClient
     */
    @Override
    public String toString() {
        return "The client is " + userName +", the host is " + hostName +
        ", the port is " + Integer.toString(port) + ", and the closeConnection status is " +
        Boolean.toString(closeConnection);
    }

    /*
     * uses command line arguments to test ClypeClient
     * Case 1: anonymous user connecting to the server represented by localhost
     *		   connection is made via the default port number.
     * case 2: ClypeClient object with username connecting to the server represented by localhost
     * Case 3: ClypeClient object with username that connects to the server represented by the hostname 
     * Case 4: a ClypeClient object with username that connects to the server represented by the host name at the port number
     */
    // public static void main(String[] args) {
    //     /*
    //      * for inputing Sherlock@128.182.90.22 in  command line as argument.Then args.length = 1
    //      * if we only type Sherlock instead of Sherlock@128.182.90.22. The args.length is still 1
    //      * for any args.length >1, outputting error message
    //      */
    //     if( args.length == 0 ) {
    //         //case 1
    //         //System.out.println("client1 created."); //debugging
    //         ClypeClient client1 = new ClypeClient();
    //         client1.start();
    //     } 
    //     else if( args.length == 1 ){
    //         //System.out.println(args.length);//debugging        	
    //         String arg1 = args[0];
    //         //System.out.println(arg1);//debugging
    //         if( arg1.contains("@") ) {
    //             String[] two_argument = arg1.split("@");
    //             arg1 = two_argument[0];
    //             String arg2 = two_argument[1];
    //             //case 2
    //             if( arg2.contains(":") ) {
    //                 String[] three_argument = arg2.split(":");
    //                 arg2 = three_argument[0];
    //                 int arg3 = Integer.parseInt( three_argument[1] );
    //                 //case 4
    //                 ClypeClient client4 = new ClypeClient( arg1, arg2, arg3 );
    //                 //System.out.println(client4);//debugging
    //                 client4.start();
    //             }else {
    //                 //case 3
    //                 ClypeClient client3 = new ClypeClient( arg1, arg2 );
    //                 //System.out.println(client3);//debugging
    //                 client3.start();

    //             }
    //         }else {
    //             //case 2           	
    //             ClypeClient client2 = new ClypeClient( arg1 );
    //             //System.out.println(client2);//debugging
    //             client2.start();
    //         }
    //     }else {
    //         System.err.println("Input argument is invalid.");
    //     }

    // }

}
