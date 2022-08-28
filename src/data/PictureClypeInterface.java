package data;

import java.io.File;

public interface PictureClypeInterface {
    // method signatures

    public File getPicture();
    //override the way to get data with key

    public String getData();
    //implement the way to get data

    public int hashCode();
    //override the way to get hashcode

    public boolean equals(Object other);
    //override the way to check if two objects are equal

    public String toString();
    //override the way to get object description

    public String getData(String key);
    //override the way to get data with key

}
