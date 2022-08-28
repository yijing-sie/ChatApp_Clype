package main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import data.ClypeData;
import data.MessageClypeData;

public class ServerSideClientIO implements Runnable{

    private volatile boolean closeConnection;// Represent whether connection is closed or not

    private ClypeServer clypeServer;// Represent the master server
    private Socket clientSocket;// Represent the socket accepted from the client
    private ClypeData dataToReceiveFromClient;// Represent data received from the client
    private ClypeData dataToSendToClient;// Represent data sent to client
    private ObjectInputStream inFromClient;// Receive information from client
    private ObjectOutputStream outToClient;// Send information to client
    private String clientName;// Represents this client's name
    private ArrayList<String> clientList;//an ArrayList consisting of clients' names
    private Object objectdata;

    /**
     * Set the ClypeServer instance variable `server`  and Socket instance variable  `clientSocket`
     * respectively to the value of the `clypeServer`  and `clientSocket`  fed as parameters 
     * to it.
     * 
     * The  closeConnection  variable is initialized to false,
     * and all other variables are initialized to null.
     * 
     * @param clypeServer	representing the master server
     * @param clientSocket	representing the socket accepted from the client
     */
    public ServerSideClientIO(ClypeServer clypeServer, Socket clientSocket ){
        this.clypeServer = clypeServer;
        this.clientSocket = clientSocket;
        clientList = new ArrayList<String>();
        closeConnection = false;
        dataToReceiveFromClient = null;
        dataToSendToClient = null;
        inFromClient = null;
        outToClient = null;
        objectdata= null;
        
    }

    // Override the run() for Runnable
    @Override
    public void run() {
        try { 
            outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromClient = new ObjectInputStream(clientSocket.getInputStream());
        }catch(IOException ioe) {
            clypeServer.broadcastServer("IOException occurred while getting I/O stream from clientSocket");
            System.err.println("IOException occurred while getting I/O stream from clientSocket");
        }
        while(!closeConnection) {
            receiveData();
            if(!closeConnection && dataToSendToClient != null){
                //System.out.println("this is the data broadcast to client:" + dataToSendToClient);//debugging
                clypeServer.broadcast(dataToSendToClient);        	
            }
        }
    }

    /**
     * Receive data from the client
     */
    public void receiveData() {	
        dataToSendToClient = null;
        try {
        	objectdata = inFromClient.readObject();
            dataToReceiveFromClient = (ClypeData) objectdata;
            if (dataToReceiveFromClient.getType() == ClypeData.LOG_OUT) {
                clypeServer.remove(this); // Remove the client from ServerSideClientIO
                clypeServer.removeClientName(clientName); // Remove the client from clientList
                // Update friendlist in the chat room
                clientList = clypeServer.getClientList();
                String allClientNames = "";
                for (String c : clientList ) {
                    allClientNames += c + "\n --- \n" ;
                }  
                clypeServer.broadcast(new MessageClypeData("server", allClientNames, ClypeData.LIST_USERS)); 
                closeConnection = true;
                // Update the removing information to the server window
                clypeServer.broadcastServer("Client closed connection --- Removing " + clientName);
                System.err.println("Client closed connection --- Removing " + clientName);
                try {
                    outToClient.close();
                    inFromClient.close();
                    clientSocket.close();                   
                } catch (IOException e) {
                    clypeServer.broadcastServer("IOException occurren while closing IOstream for : " + clientName);
                    System.err.println("IOException occurren while closing IOstream from client or clientSocket");
                }                
            }                  
            else if(dataToReceiveFromClient.getData().equals("This is user name")) {
            		clientName = dataToReceiveFromClient.getUserName();
                    clypeServer.addClientName(clientName); // Add the client from clientList
                    // Update the removing information to the server window
                    clypeServer.broadcastServer(clientName + " connected!");
                    clientList = clypeServer.getClientList();
                    String allClientNames = "";
                    for (String c : clientList ) {
                        allClientNames += c + "\n --- \n" ;
                    }             
                    dataToSendToClient = new MessageClypeData("server", allClientNames, ClypeData.LIST_USERS);
//                    System.out.println("this is the data sended to client:" + dataToSendToClient);//debugging
            } 
            else if (dataToReceiveFromClient.getType() == ClypeData.SEND_A_MESSAGE) {
                dataToSendToClient = dataToReceiveFromClient; 
            } 
            else if (dataToReceiveFromClient.getType() == ClypeData.SEND_A_FILE) {
                dataToSendToClient = dataToReceiveFromClient;
            } 
            else if (dataToReceiveFromClient.getType() == ClypeData.SEND_A_PICTURE) {
                dataToSendToClient = dataToReceiveFromClient;
            } 
            else {
                clypeServer.broadcastServer(dataToReceiveFromClient.getUserName() + "'s request : No Type Matched!.");
                System.err.println("No Type Matched!.");
            }
        } catch (ClassNotFoundException cnfe) {
            clypeServer.broadcastServer( clientName + "'s request' : Class not locatable.");
            System.err.println("Class not locatable.");
        } catch (IOException ioe) {
            if( ioe.getMessage() == null ) {	
                clypeServer.broadcastServer(clientName + "'s request' : IOException occurred while receiving data : " + ioe.getMessage());
                System.err.println("IOException occurred while receiving data : " + ioe.getMessage());
            }
        }catch(ClassCastException c) {
            clypeServer.broadcastServer(clientName + "'s request' :Class not castable.");
			System.err.println("Class not castable.");
        }catch(NullPointerException npe) {
            clypeServer.broadcastServer(clientName + "'s request' : FILE NOT FOUND.");
            System.err.println("FILE NOT FOUND.");
        }
    }


    /**
     *  Close the connection fot the server
     */
    public void closeServerSideClientIO () {
        closeConnection = true;
        try {
            outToClient.close();
            inFromClient.close();
            clientSocket.close();                   
        } catch (IOException e) {
            System.err.println("IOException occurren while closing IOstream from client or clientSocket");
        } 
    }

    /**
     * Send the data to the client
     * @param data ClypeData to be sent to the client
     */
    public void sendData(ClypeData data) {
        if (!closeConnection) {
            try {
                outToClient.writeObject(data);
                //System.out.println("this is the data sended to client:" + dataToSendToClient);//debugging
            }catch(IOException ioe) {
                clypeServer.broadcastServer("IOException occurred while sending data to " + clientName + " : " + ioe.getMessage());
                System.err.println("IOException occurred while sending data : " + ioe.getMessage());
            }
        }
    }
    
    /**
     * Accessor for userName
     * @return user name
     */
    public String getClientName() {
        return clientName;
    }
}
