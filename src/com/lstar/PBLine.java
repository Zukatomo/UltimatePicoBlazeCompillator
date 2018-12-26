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
        defNameregs();
        defCalls();
    }

    public void generateInstuctionBits(){
        if(runnableLine){
            try {
                //System.out.println(normalizedBase);
                bits = new InstructionBits(normalizedBase);
            } catch (Exception e) {
                if(e instanceof Erro){
                    ((Erro) e).Err(rowNumber, normalizedBase);
                    //e.printStackTrace();
                }else {
                    System.out.println("\u001B[32m" + e.toString() + " at line: " + rowNumber + "\u001B[0m");
                    System.out.println(e.toString() + " at line: " + rowNumber);
                    e.printStackTrace();
                }
            }
        }
    }

    public int getIntBits(){
        if (bits!=null)
            return bits.getInstructionInt();
        else return 0;
    }

    private void defCalls() {
        if(normalizedBase == null) return;
        if(!normalizedBase.contains(":")) return;

        String tokens[] = normalizedBase.split(":");
        if(tokens.length == 1){
            String callName = tokens[0].trim();
            if(callName.contains(" ")){
                Erro.def(rowNumber + "Call cant contains space");
                return;
            }
            if(PicoBlazeCompillator.calls.containsKey(callName)){
                Erro.alreadyDefined(rowNumber, "Call Name");
                return;
            }
            PicoBlazeCompillator.calls.put(callName, 0);
            this.callName = callName;
            runnableLine = false;
        }
        if(tokens.length == 2){
            String callName = tokens[0].trim();
            if(callName.contains(" ")){
                Erro.def(rowNumber +"Call cant contains space");
                return;
            }
            if(PicoBlazeCompillator.calls.containsKey(callName)){
                Erro.alreadyDefined(rowNumber, "Call Name");
                return;
            }
            PicoBlazeCompillator.calls.put(callName, 0);
            this.callName = callName;

            normalizedBase = tokens[1].trim();
        }
        if (tokens.length > 2 ){
            Erro.def(rowNumber + "Multiple calls i a row");
            return;
        }

    }

    private void defNameregs() {
        if(normalizedBase == null) return;

        String tokens[] = normalizedBase.split(" ");
        if(tokens[0].trim().toUpperCase().equals("NAMEREG")){
            if(tokens.length != 3){
                Erro.noEnoughParameter(rowNumber,"Namereg");
                return;
            }
            //Check for correct format
            String op1 = tokens[1].trim().toUpperCase();
            String op2 = tokens[2].trim();

            if(!(op1.matches("S([0-9]|[A-F])"))){
                Erro.numberNotFormatedcorrect(rowNumber, normalizedBase);
                return;
            }
            if((op2.matches("([A-F]|[0-9]){2}") || op2.matches("S([0-9]|[A-F])"))){
                Erro.numberNotFormatedcorrect(rowNumber, normalizedBase);
                return;
            }

            if(PicoBlazeCompillator.constants.containsKey(op2)){
                Erro.alreadyDefined(rowNumber, normalizedBase);
                return;
            }

            if(PicoBlazeCompillator.nameregs.containsKey(op2)){
                Erro.alreadyDefined(rowNumber, "Namereg");
                return;
            }else {
                PicoBlazeCompillator.nameregs.put(op2,op1.toUpperCase());
                runnableLine = false;
            }
        }
    }

    private void defConstants() {
        if( normalizedBase == null ) return;

        String tokens[] = normalizedBase.split(" ");
        if(tokens[0].trim().toUpperCase().equals("CONSTANT")){
            if(tokens.length != 3){
                Erro.noEnoughParameter(rowNumber,"Constant");
                return;
            }

            //Check for correct format
            String op1 = tokens[1].trim();
            String op2 = tokens[2].trim().toUpperCase();

            if(op1.matches("([A-F]|[0-9]){2}")){
                Erro.numberNotFormatedcorrect(rowNumber, normalizedBase);
                return;
            }
            if(!(op2.matches("([A-F]|[0-9]){2}"))){
                Erro.numberNotFormatedcorrect(rowNumber, normalizedBase);
                return;
            }

            if(PicoBlazeCompillator.constants.containsKey(op1)){
                Erro.alreadyDefined(rowNumber, "Constant");
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

    public void setCompileRowNumber(int n){
        this.complieRowNumber = n;
    }

    public String getCallName() {
        return callName;
    }

    public boolean isRunnableLine() {
        return runnableLine;
    }

    public int getRowNumber() {
        return rowNumber;
    }
}
