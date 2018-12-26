package com.lstar;

public class PBLine {
    private int rowNumber;
    private int complieRowNumber;
    private String base;
    private String normalizedBase;
    private String comment;
    private boolean runnableLine;
    private String callName;
    private InstructionBits bits;

    PBLine(int rowNumber, String base){
        this.rowNumber = rowNumber;
        this.base = base;

        normalize();
        defConstants();
    }

    private void defConstants() {
        if( normalizedBase == null ) return;

        String tokens[] = normalizedBase.split(" ");
        if(tokens[0].trim().toUpperCase().equals("CONSTANT")){
            if(tokens.length != 3){
                // TODO Erro.noEnoughParameter(rowNumber,"Constant");
                return;
            }

            //Check for correct format
            String op1 = tokens[1].trim();
            String op2 = tokens[2].trim().toUpperCase();

            if(op1.matches("([A-F]|[0-9]){2}")){
                //TODO incorrect format
                return;
            }
            if(!(op2.matches("([A-F]|[0-9]){2}"))){
                //TODO incorrect format
                return;
            }

            if(PicoBlazeCompillator.constants.containsKey(op1)){
                // TODO Erro.alreadyDefined(rowNumber, "Constant");
                return;
            }else {
                PicoBlazeCompillator.constants.put(op1,op2);
                runnableLine = false;
            }
        }
    }

    private void normalize() {
        if(base.contains(";")){
            comment = base.split(";")[1];
            normalizedBase = base.split(";")[0];
        }else {
            normalizedBase = base;
        }

        //Remove all blank characters
        normalizedBase = normalizedBase.replaceAll("\\(", " ");
        normalizedBase = normalizedBase.replaceAll("\\)", " ");
        normalizedBase = normalizedBase.replaceAll("\t", " ");
        normalizedBase = normalizedBase.replaceAll(",", " ");
        normalizedBase = normalizedBase.replaceAll(" +", " ");
        normalizedBase = normalizedBase.trim();
        if(normalizedBase.length() > 1){
            runnableLine = true;
        }else{
            normalizedBase = null;
        }
    }
}
