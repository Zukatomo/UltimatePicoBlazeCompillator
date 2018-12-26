package com.lstar;

import java.io.File;
import java.util.*;

public class PicoBlazeCompillator {

    public static Map<String, String> constants;
    public static Map<String, String> nameregs;
    public static Map<String, Integer> calls;
    public static List<PBLine> lines;

    private int currentRowNumber = 1;
    private int compillableRowNumber=0;


    PicoBlazeCompillator(String filename){
        if(filename.equals("HARLEM")){
            System.out.println("HARLEM");

        }else {

            constants = new TreeMap<String, String>();
            nameregs = new TreeMap<String, String>();
            calls = new TreeMap<String, Integer>();
            lines = new ArrayList<PBLine>();

            readFile(filename);
            System.out.println(constants.toString());

        }

    }

    private void readFile(String filename){
        Scanner sc = null;
        try {
            sc = new Scanner(new File(filename));
        }catch (Exception ex){
            //TODO file exception
            return;
        }
        while(sc.hasNextLine()){
            lines.add(new PBLine(currentRowNumber++, sc.nextLine()));
        }
    }
}
