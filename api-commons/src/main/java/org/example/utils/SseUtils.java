package org.example.utils;

public class SseUtils {


    String Userid;
    String identity;


    public static String gennerate (String userid,String identity){
        return userid+"$"+identity;
    }
}
