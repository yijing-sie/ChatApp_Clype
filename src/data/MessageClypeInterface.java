package data;

public interface MessageClypeInterface {
    // method signatures

    public String getData();
    //implement the way to get data

    public int hashCode();
    //override the way to get hashcode

    public boolean equals(Object other);
    //override the way to check if two objects are equal

    public String toString();
    //override the way to get object description

    public String getData(String key);
    //override the way to decrypt the string in ‘message’ and return the decrypted string.

}
