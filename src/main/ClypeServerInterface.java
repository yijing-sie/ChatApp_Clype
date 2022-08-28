package main;

public interface ClypeServerInterface {
    // method signatures
    
    public String start();
    //implement the way to start

    public int getPort();
    //implement the way to get port

    public int hashCode();
    //override the way to get hashcode

    public boolean equals(Object other);
    //override the way to check if two objects are equal

    public String toString();
    //override the way to get object description
    

}
