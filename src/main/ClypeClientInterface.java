package main;

import javafx.util.Pair;

public interface ClypeClientInterface {
    // method signatures

    public boolean start();
    //implement the way to reads data from the client, and print the data out.

    public void readClientMessage(String data);
    //implement the way to read client's message

    public void sendData();
    //implement the way to send data to server

    public void receiveData();
    //implement the way to receive data from the server

    public Pair<Boolean, String> printData();
    //implement the way to print the received data to standard output,

    public String getUserName();
    //implement the way to get userName

    public String getHostName();
    //implement the way to get hostName

    public int getPort();
    //implement the way to get port

    public int hashCode();
    //override the way to get hashcode

    public boolean equals(Object other);
    //override the way to check if two objects are equal

    public String toString();
    //override the way to get object description

}
