package com.lstar;

import java.io.File;
import java.io.PrintWriter;
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
            Messages.p("Maps initialized OK");
            Messages.p("Errors: ");

            readFile(filename);
            assignComRowNum();
            genInstBits();

        }

    }

    public void printToFile(){
        PrintWriter pw = null;
        try {
            pw = new PrintWriter("out.VHD", "UTF-8");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        String header = "library IEEE;\n" +
                "use IEEE.STD_LOGIC_1164.ALL;\n" +
                "use IEEE.STD_LOGIC_ARITH.ALL;\n" +
                "use IEEE.STD_LOGIC_UNSIGNED.ALL;";
        header += "\nlibrary unisim;\n" +
                "use unisim.vcomponents.all;";
        header += "\nentity lcd_prg is\n" +
                "    Port (      addressA : in std_logic_vector(9 downto 0);\n" +
                " addressB : in std_logic_vector(9 downto 0);\n" +
                "                instructionA : out std_logic_vector(17 downto 0);\n" +
                "  instructionB : out std_logic_vector(17 downto 0);\n" +
                "               clk : in std_logic);\n" +
                "    end lcd_prg;\n" +
                "architecture low_level_definition of lcd_prg is";
        pw.println(header);
        for(int i = 0;i<64;i++){
            //System.out.println("attribute INIT_"+String.format("%2s",Integer.toHexString(i).toUpperCase()).replaceAll(" ", "0") +" : string;");
            pw.println("attribute INIT_"+String.format("%2s",Integer.toHexString(i).toUpperCase()).replaceAll(" ", "0") +" : string;");
        }
        for(int i = 0;i<8;i++){
            pw.println("attribute INITP_"+String.format("%2s",Integer.toHexString(i).toUpperCase()).replaceAll(" ", "0") +" : string;");
        }

        StringBuilder allBits = new StringBuilder();
        StringBuilder parityBits = new StringBuilder();
        StringBuilder mainBits = new StringBuilder();
        for(PBLine elem:lines) {
            if (elem.isRunnableLine()) {
                //System.out.println(String.format("%5s",Integer.toHexString(elem.getIntBits()).toUpperCase()).replaceAll(" ", "0"));
                allBits.insert(0, String.format("%18s", Integer.toBinaryString(elem.getIntBits()).toUpperCase()).replaceAll(" ", "0"));
            }
        }
        String k2 = allBits.toString();
        //k2 = String.format("%"+((64*120)-k2.length())+"s", k2).replaceAll(" ", "0");
        while (k2.length() > 0 ){
            mainBits.insert(0, k2.substring(k2.length()-16));
            k2 = k2.substring(0, k2.length()-16);
            parityBits.insert(0, k2.substring(k2.length()-2));
            k2 = k2.substring(0, k2.length()-2);
        }
        //System.out.println(mainBits.toString());
        //System.out.println(parityBits.toString());

        StringBuilder mainBitsHexa = new StringBuilder();
        StringBuilder parityBitsHexa = new StringBuilder();
        String temp = mainBits.toString();
        while (temp.length() >0){
            mainBitsHexa.insert(0, String.format("%4s",Integer.toHexString(Integer.parseInt(temp.substring(temp.length()-16),2))).replaceAll(" ", "0").toUpperCase());
            temp = temp.substring(0, temp.length()-16);
        }

        temp = parityBits.toString();
        //System.out.println(temp.length());
        while(temp.length()%4!=0){
            temp = "0" + temp;
        }
        while(temp.length() > 0){
            parityBitsHexa.insert(0, String.format("%1s", Integer.toHexString(Integer.parseInt(temp.substring(temp.length()-4),2))).replaceAll(" ", "0").toUpperCase());
            temp = temp.substring(0, temp.length()-4);
        }
        //System.out.println(temp);

        //System.out.println(mainBitsHexa.toString());
        //System.out.println(parityBitsHexa.toString());

        //System.out.println(mainBits.toString());

        /*StringBuilder mainBits = new StringBuilder();
        for(PBLine elem:lines){
            if(elem.isRunnableLine()) {
                //System.out.println(String.format("%5s",Integer.toHexString(elem.getIntBits()).toUpperCase()).replaceAll(" ", "0"));
                mainBits.insert(0,String.format("%5s",Integer.toHexString(elem.getIntBits()).toUpperCase()).replaceAll(" ", "0"));
            }
        }
        //System.out.println(mainBits.length());
        //System.out.println(String.format("%"+(64*64-mainBits.length())+"s", mainBits.toString()).replaceAll(" ", "0"));
        /*
        String instructionsB = String.format("%"+((64*64)-mainBits.length())+"s", mainBits.toString()).replaceAll(" ", "0");
        */
        String instructionsB = mainBitsHexa.toString();
        instructionsB = "0000000000000000000000000000000000000000000000000000000000000000" + instructionsB;
        for (int i = 0; i < 64; i++){
            String number = String.format("%2s",Integer.toHexString(i).toUpperCase()).replaceAll(" ", "0");
            String bits = "0000000000000000000000000000000000000000000000000000000000000000";
            if(instructionsB.length() > 64) {
                bits = instructionsB.substring(instructionsB.length() - 64);
                instructionsB = instructionsB.substring(0, instructionsB.length() - 64);
            }
            pw.println("attribute INIT_"+number+" of ram_1024_x_18  : label is \""+ bits +"\";");
        }

        instructionsB = parityBitsHexa.toString();
        instructionsB = "0000000000000000000000000000000000000000000000000000000000000000" + instructionsB;
        for (int i = 0; i < 8; i++){
            String number = String.format("%2s",Integer.toHexString(i).toUpperCase()).replaceAll(" ", "0");
            String bits = "0000000000000000000000000000000000000000000000000000000000000000";
            if(instructionsB.length() > 64) {
                bits = instructionsB.substring(instructionsB.length() - 64);
                instructionsB = instructionsB.substring(0, instructionsB.length() - 64);
            }
            pw.println("attribute INITP_"+number+" of ram_1024_x_18 : label is \""+ bits +"\";");
        }



        String las2 = "begin\n" +
                "--\n" +
                "  --Instantiate the Xilinx primitive for a block RAM\n" +
                "  ram_1024_x_18: RAMB16_S18_S18\n" +
                "  --synthesis translate_off\n" +
                "  --INIT values repeated to define contents for functional simulation";
        pw.println(las2);
        instructionsB = mainBitsHexa.toString();
        instructionsB = "0000000000000000000000000000000000000000000000000000000000000000" + instructionsB;

        pw.print("generic map ( ");
        for (int i = 0; i < 64; i++){
            String number = String.format("%2s",Integer.toHexString(i).toUpperCase()).replaceAll(" ", "0");
            String bits = "0000000000000000000000000000000000000000000000000000000000000000";
            if(instructionsB.length() > 64) {
                bits = instructionsB.substring(instructionsB.length() - 64);
                instructionsB = instructionsB.substring(0, instructionsB.length() - 64);
            }
            if(i == 0) {
                pw.println("INIT_" + number + " => X\"" + bits + "\",");
            }else{
                pw.println("\t\t\t  INIT_" + number + " => X\"" + bits + "\",");
            }
        }

        instructionsB = parityBitsHexa.toString();
        instructionsB = "0000000000000000000000000000000000000000000000000000000000000000" + instructionsB;
        for (int i = 0; i < 8; i++){
            String number = String.format("%2s",Integer.toHexString(i).toUpperCase()).replaceAll(" ", "0");
            String bits = "0000000000000000000000000000000000000000000000000000000000000000";
            if(instructionsB.length() > 64) {
                bits = instructionsB.substring(instructionsB.length() - 64);
                instructionsB = instructionsB.substring(0, instructionsB.length() - 64);
            }
            if(i!=7) {
                pw.println("             INITP_" + number + " => X\"" + bits + "\",");
            }else{
                pw.println("             INITP_" + number + " => X\"" + bits + "\")");
            }
        }



        String end = "  --synthesis translate_on\n" +
                "port map(    DIA => \"0000000000000000\",\n" +
                "              DIPA => \"00\",\n" +
                "               ENA => '1',\n" +
                "               WEA => '0',\n" +
                "              SSRA => '0',\n" +
                "              CLKA => clk,\n" +
                "             ADDRA => addressA,\n" +
                "               DOA => instructionA(15 downto 0),\n" +
                "              DOPA => instructionA(17 downto 16),\n" +
                "      DIB => \"0000000000000000\",\n" +
                "              DIPB => \"00\",\n" +
                "               ENB => '1',\n" +
                "               WEB => '0',\n" +
                "              SSRB => '0',\n" +
                "              CLKB => clk,\n" +
                "             ADDRB => addressB,\n" +
                "               DOB => instructionB(15 downto 0),\n" +
                "              DOPB => instructionB(17 downto 16));  \n" +
                "--\n" +
                "end low_level_definition;\n" +
                "--\n" +
                "------------------------------------------------------------------------------------\n" +
                "--\n" +
                "-- END OF FILE lcd_prg.vhd\n" +
                "--\n" +
                "------------------------------------------------------------------------------------\n";
        pw.println(end);

        //System.out.println("last character: " + alma.substring(alma.length() - 64));

        pw.close();

    }

    public void printIntBits(){
        for(PBLine elem:lines){
            if(elem.isRunnableLine()) {
                System.out.print(elem.getRowNumber() + "\t" + elem.getIntBits() + "\t\t");
                System.out.println(Integer.toHexString(elem.getIntBits()).toUpperCase());
            }
        }
    }

    private void genInstBits(){
        for(PBLine elem:lines){
            elem.generateInstuctionBits();
        }
    }

    private void assignComRowNum(){
        for(PBLine elem : lines){
            if(elem.getCallName() != null){
                if(calls.containsKey(elem.getCallName())){
                    calls.put(elem.getCallName(), compillableRowNumber);
                }else{
                    Erro.notDefined(elem.getRowNumber(), "call: " + elem.getCallName());
                }
            }
            if(elem.isRunnableLine()){
                elem.setCompileRowNumber(compillableRowNumber++);
            }
        }
    }

    private void readFile(String filename){
        Scanner sc = null;
        try {
            sc = new Scanner(new File(filename));
        }catch (Exception ex){
            Erro.noFile();
            return;
        }
        while(sc.hasNextLine()){
            lines.add(new PBLine(currentRowNumber++, sc.nextLine()));
        }
    }
}
