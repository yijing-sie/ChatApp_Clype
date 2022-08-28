package data;

import java.util.Date;

public interface ClypeDataInterface {
    // method signatures

    public int getType();
    //implement the way to get type

    public String getUserName();
    //implement the way to get userName

    public Date getDate();
    //implement the way to get date

    public abstract String getData();
    //an abstract way to get date

    public abstract String getData(String key);

}
