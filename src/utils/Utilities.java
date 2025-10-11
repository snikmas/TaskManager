package utils;

import tasks.Status;

public class Utilities {

    public static void statusMenu(){
        for(int i = 0; i < Status.values().length; i++){
            System.out.println((i + 1) + Status.values()[i].name());
        }
    }

}
