package main;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import application.Server;
import data.ClypeData;
/**
 * Class which represents the server, containing the port number to connect to, a boolean representing whether the server needs to
 * be closed or not
 * 
 * @author Yijing
 *
 */
public class ClypeServer implements ClypeServerInterface{
    private int port;// Integer which represents port number on server connected to
    private volatile boolean closeConnection;// Boolean which represents whether connection is closed or not
    private ArrayList<ServerSideClientIO> serverSideClientIOList;// An ArrayList consisting of ServerSideClientIO objects
    private ArrayList<String> clientList;// An ArrayList consisting of clients' names
    private Server server; // The server application
    private ServerSocket serverSocket;

    /**
     * Constructor for the server application and a custom port number with default dataToReceiveFromClient as null 
     * and default dataToSendToClient as null
     * 
     * @param port  integer which represents port number on server connected to
     * @throws IllegalArgumentException port number less than 1024 
     */
    public ClypeServer(int port, Server server) throws IllegalArgumentException{
        if( port < 1024 ) throw new IllegalArgumentException("port shouldn't be lesser than 1024.");
        else this.port = port;
        this.server = server;
        closeConnection = false;
        serverSideClientIOList = new ArrayList<ServerSideClientIO>();
        clientList = new ArrayList<String>();
        
    }

    /**
     * Constructor for the server application with a default port number 7000, 
     * dataToReceiveFromClient null and dataToSendToClient null
     */
    public ClypeServer(Server server) {
        this(7000, server);
    }

    /**
     * Start the connection
     */
    public String start() {
        try {       	
            serverSocket = new ServerSocket( port );
            while(!closeConnection) {
                Socket client  = serverSocket.accept();
                ServerSideClientIO serverSideClient = new ServerSideClientIO(this, client);                
                serverSideClientIOList.add(serverSideClient);
                Thread serverSideClientThread = new Thread( serverSideClient );
                serverSideClientThread.start();
                try {
					Thread.sleep(1000);// Wait for serverSideClientThread to update clientName before accepting another client
				} catch (InterruptedException e) {
					System.err.println("InterruptedException occurred while server main Thread sleeping");
				}                     
            }                                   
            serverSocket.close();
            System.out.println("Connection closed.");            	
        } catch (IOException ioe) {
            return ioe.getMessage();
        }

        return "Connection closed.";
    }

    /**
     * Close the connection
     */
    public synchronized void closeClypeServer() {
        this.closeConnection = true; // Set closeConnection to true
        for (ServerSideClientIO serverSideClientIO : serverSideClientIOList ) {
            serverSideClientIO.closeServerSideClientIO();
        }
        try {
            serverSocket.close();
            System.out.println("Socket closed");
        } catch (IOException ioe) {
            ioe.getMessage();
        }
    }


    /**
     * A synchronized method that iterates through the list `serverSideClientIOList` to send the data to each client
     * @param dataToBroadcastToClients  Clypedata that will be broadcast to other clients
     */
    public synchronized void broadcast(ClypeData dataToBroadcastToClients) {
        for (ServerSideClientIO serverSideClientIO : serverSideClientIOList ) {
            serverSideClientIO.sendData(dataToBroadcastToClients);
        }

    }
    /**
     * Send the message to the application to update the information for the server window
     * @param dataToBroadcastToClients  message that will be added to display on the server window
     */
    public void broadcastServer(String message) {
        server.setLabel(message);
    }

    /**
     * A synchronized method that adds a new client name to the `clientList`
     */
    public synchronized void addClientName(String userName) {
    	clientList.add(userName);
    }

    /**
     * A synchronized method that removes `userName` from the `clientList`
     * @param userName client name to be removed 
     */
    public synchronized void removeClientName(String userName) {
    	clientList.remove(userName);
    }
    
    /**
     * A synchronized method that takes in a single ServerSideClientIO object, 
     * and removes this object from the list `serverSideClientIOList`.
     * @param serverSideClientToRemove  ServerSideClientIO object that will be removed from the `serverSideClientIOList`
     */
    public synchronized void remove(ServerSideClientIO serverSideClientToRemove) {
        serverSideClientIOList.remove(serverSideClientToRemove);
    }

    /**
     * Accessor for the `clientList`
     * @return an arraylist representing the clientList
     */
    public ArrayList<String> getClientList(){
        return clientList;
    }

    /**
     * Accessor for the port number
     * 
     * @return  integer which represents port number server connected to
     */
    public int getPort() {
        return port;
    }

    /**
     * hashCode() method overridden from
     * java.lang.Object to returen a value of hash code for the object
     * 
     * @return  value of hash code
     */
    @Override
    public int hashCode() {
        int result = 17;
        int c = port;
        result = 37 * result + c;
        c = closeConnection ? 1:0;	
        result = 37 * result + c;
        return result;
    }

    /**
     * equals() method overridden from java.lang.Object
     * 
     * @return  True if the two objects are equal when they are both ClypeServer with the same port number and connection status
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ClypeServer)) return false;
        // if (other == null ) return false;
        ClypeServer otherServer = (ClypeServer) other;
        return this.closeConnection == otherServer.closeConnection &&
        this.port == otherServer.port;
    }

    /**
     * toString() method overridden from
     * java.lang.Object to return a description for ClypeServer
     * 
     * @return   full description for ClypeServer
     */
    @Override
    public String toString() {
        return "The port is : " + Integer.toString(port) + ",and the status of the closeconnection is : "
        + Boolean.toString(closeConnection);
    }

    // /*
    //  * uses command line arguments to test ClypeServer
    //  * Case 1: create a ClypeServer object with the default port.
    //  * Case 2: take a single argument representing a port number
    //  * 
    //  */
    // public static void main(String[] args) {
    //     if( args.length == 0 ) {
    //         //case 1
    //         ClypeServer server1 = new ClypeServer();
    //         server1.start();
    //     } 
    //     else if( args.length == 1 ){
    //         int arg1 = Integer.parseInt( args[0] );
    //         //case 2
    //         ClypeServer server2 = new ClypeServer( arg1 );
    //         server2.start();
    //     }else {
    //         //for args.length >1, outputting error message
    //         System.err.println("Input argument is invalid.");
    //     }

    // }

}

