package com.lstar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("File name: ");
        String name = null;
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        try {
            name = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PicoBlazeCompillator c = new PicoBlazeCompillator(name);
    }
}
