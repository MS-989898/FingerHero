package com.example.fingerhero;

public class UserInformation {
    String code = "";
    String name = "";
    long time;

    public UserInformation()  {
        time = System.currentTimeMillis();
    }
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

}
