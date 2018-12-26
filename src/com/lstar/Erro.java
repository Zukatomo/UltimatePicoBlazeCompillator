package com.lstar;

public class Erro extends Exception{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    int id = 0;
    String m = "";

    public Erro(int id){
        this.id = id;
    }
    public Erro(int id, String m){
        this.id = id;
        this.m = m;
    }

    public void Err(int row, String message){
        switch (id){
            case 0: noFile(); break;
            case 1: noEnoughParameter(row, message); break;
            case 2: toManyParameter(row, message); break;
            case 3: alreadyDefined(row, message); break;
            case 4: notDefined(row, message); break;
            case 5: numberNotFormatedcorrect(row, message);break;
            case 6: alreadyDefinedReg(row, message);break;
        }
    }

    public static void def(String m){
        System.out.println(ANSI_RED + "ERROR: " + ANSI_WHITE + m + ANSI_RESET);
    }
    //0
    public static void noFile(){
        System.out.println(ANSI_RED + "ERROR: " + ANSI_WHITE +"No such file, or cant be read" + ANSI_RESET);
    }

    //1
    public static void noEnoughParameter(int row, String m){
        System.out.println(ANSI_RED + "ERROR: " + ANSI_WHITE +"Not enough parameters in row:" +row + "\t"+ANSI_GREEN+m  + ANSI_RESET);
    }
    //2
    public static void toManyParameter(int row, String m){
        System.out.println(ANSI_RED + "ERROR: " + ANSI_WHITE +"To many parameters in row:" +row + "\t"+ANSI_GREEN+m  + ANSI_RESET);
    }

    //3
    public static void alreadyDefined(int row, String m){
        System.out.println(ANSI_RED + "ERROR: " + ANSI_WHITE +"Already defined at line: " + row + "\t"+ANSI_GREEN+m +ANSI_RESET);
    }

    //4
    public static void notDefined(int row, String m){
        System.out.println(ANSI_RED + "ERROR: " + ANSI_WHITE +"Not defined at line: " + row + "\t"+ANSI_GREEN+m+ANSI_RESET);
    }

    //5
    public static void numberNotFormatedcorrect(int row, String m){
        System.out.println(ANSI_RED + "ERROR: " + ANSI_WHITE +"Number not formated correct at line: " + row + "\t"+ANSI_GREEN+m+ANSI_RESET);
    }

    //6
    public static void alreadyDefinedReg(int row, String m){
        System.out.println(ANSI_RED + "ERROR: " + ANSI_WHITE +"Already difined as a register: " + row + "\t"+ANSI_GREEN+m+ANSI_RESET);
    }
}
