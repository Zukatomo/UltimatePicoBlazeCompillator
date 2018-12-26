package com.lstar;

public class InstructionBits {


    private String instruction;
    private String bits;

    public InstructionBits(String instruction) throws Exception {
        this.instruction = instruction;
        generateBits();
        //System.out.println(bits);
    }

    private void generateBits() throws Exception {
        if(instruction == null) return;
        String tokens[] = instruction.split(" ");
        String op1 = null;
        String op2 = null;

        String instr = tokens[0].trim().toUpperCase();
        if(tokens.length >= 2) op1 = tokens[1].trim();
        if(tokens.length >= 3) op2 = tokens[2].trim();
        int nu = tokens.length;

        switch (instr){

            //Data Transfer
            case "LOAD": handleArithData(0,op1,op2,nu);break;
            case "INPUT": handleArithData(4,op1,op2,nu);break;
            case "FETCH": handleArithData2(6,op1,op2,nu);break;
            case "OUTPUT": handleArithData(44,op1,op2,nu);break;
            case "STORE": handleArithData2(46,op1,op2,nu);break;

            //ARITH
            case "AND": handleArithData(10,op1,op2,nu);break;
            case "OR": handleArithData(12,op1,op2,nu);break;
            case "XOR": handleArithData(14,op1,op2,nu);break;
            case "TEST": handleArithData(18,op1,op2,nu);break;
            case "COMPARE": handleArithData(20,op1,op2,nu);break;
            case "ADD": handleArithData(24,op1,op2,nu);break;
            case "ADDCY": handleArithData(26,op1,op2,nu);break;
            case "SUB": handleArithData(28,op1,op2,nu);break;
            case "SUBCY": handleArithData(30,op1,op2,nu);break;

            //SR
            case "SR0": handleSR(instr,op1,op2,nu); break;
            case "SR1": handleSR(instr,op1,op2,nu); break;
            case "SRX": handleSR(instr,op1,op2,nu); break;
            case "SRA": handleSR(instr,op1,op2,nu); break;
            case "RR": handleSR(instr,op1,op2,nu); break;
            case "SL0": handleSR(instr,op1,op2,nu); break;
            case "SL1": handleSR(instr,op1,op2,nu); break;
            case "SLX": handleSR(instr,op1,op2,nu); break;
            case "SLA": handleSR(instr,op1,op2,nu); break;
            case "RL": handleSR(instr,op1,op2,nu); break;

            //BRANCH
            case "JUMP": handleJump(instr,op1,op2,nu); break;
            case "CALL": handleCall(instr,op1,op2,nu); break;
            case "RETURN": handleReturn(instr,op1,op2,nu); break;
            case "RETURNI": handleReturnI(instr,op1,op2,nu); break;
            case "ENINTERR": handleReturnX(instr,op1,op2,nu); break;
            case "DISINTERR": handleReturnX(instr,op1,op2,nu); break;
            default: throw new Erro(4);
        }



    }
    private void handleReturnX(String ist, String op1, String op2, int nu) throws Exception{
        if(argumentumNumberError(1,nu)){
            if(ist.toUpperCase().trim() == "ENINTERR") bits = integerTostring(60,6) + integerTostring(0,9) + "1";
            if(ist.toUpperCase().trim() == "DISINTERR") bits = integerTostring(60,6) + integerTostring(0,9) + "0";
        }
    }

    private void handleReturnI(String ist, String op1, String op2, int nu) throws Exception{
        if(argumentumNumberError(2,nu)){
            bits = integerTostring(56,6);
            bits = bits + integerTostring(0,9);
            switch (op1.toUpperCase().trim()){
                case "E": bits = bits + "1"; break;
                case "D": bits = bits + "0"; break;
                default: throw new Erro(4);
            }
        }
    }

    private void handleReturn(String ist, String op1, String op2, int nu) throws Exception{
        if(nu == 1){
            bits = integerTostring(42,6);
            bits = bits + integerTostring(0,12);
        }else {
            if(nu == 2){
                bits = integerTostring(43,6);
                switch (op1.toUpperCase().trim()){
                    case "Z": bits = bits + "00"; break;
                    case "NZ": bits = bits + "01"; break;
                    case "C": bits = bits + "10"; break;
                    case "NC": bits = bits + "11"; break;
                    default: throw new Erro(4);
                }
                bits = bits + integerTostring(0,10);
            }else{
                throw new Erro(4);
            }
        }

    }

    private void handleCall(String ist, String op1, String op2, int nu) throws Exception{
        if(nu == 2){
            bits = integerTostring(48,6);
            bits = bits + "00";
            if(!PicoBlazeCompillator.calls.containsKey(op1.trim())){
                throw new Erro(4);
            }
            bits = bits + integerTostring(PicoBlazeCompillator.calls.get(op1.trim()), 10);
        }else {
            if(argumentumNumberError(3,nu)) {
                bits = integerTostring(49, 6);
                switch (op1.toUpperCase().trim()){
                    case "Z": bits = bits + "00"; break;
                    case "NZ": bits = bits + "01"; break;
                    case "C": bits = bits + "10"; break;
                    case "NC": bits = bits + "11"; break;
                    default: throw new Erro(4);
                }
                if(!PicoBlazeCompillator.calls.containsKey(op2.trim())){
                    throw new Erro(4);
                }
                bits = bits + integerTostring(PicoBlazeCompillator.calls.get(op2.trim()), 10);
            }
        }
    }

    private void handleJump(String ist, String op1, String op2, int nu) throws Exception{
        if(nu == 2){
            bits = integerTostring(52,6);
            bits = bits + "00";
            if(!PicoBlazeCompillator.calls.containsKey(op1.trim())){
                throw new Erro(4);
            }
            bits = bits + integerTostring(PicoBlazeCompillator.calls.get(op1.trim()), 10);
        }else {
            if(argumentumNumberError(3,nu)) {
                bits = integerTostring(53, 6);
                switch (op1.toUpperCase().trim()){
                    case "Z": bits = bits + "00"; break;
                    case "NZ": bits = bits + "01"; break;
                    case "C": bits = bits + "10"; break;
                    case "NC": bits = bits + "11"; break;
                    default: throw new Erro(4);
                }
                if(!PicoBlazeCompillator.calls.containsKey(op2.trim())){
                    throw new Erro(4);
                }
                bits = bits + integerTostring(PicoBlazeCompillator.calls.get(op2.trim()), 10);
            }
        }

    }

    private void handleSR(String ist, String op1, String op2, int nu) throws Exception {
        if(argumentumNumberError(2,nu)){

            bits = integerTostring(32,6);
            bits = bits + integerTostring(getRegisterNumber(op1),4);
            bits = bits + integerTostring(0,4);
            switch (ist){
                case "SR0": bits = bits + "1110"; break;
                case "SR1": bits = bits + "1111"; break;
                case "SRX": bits = bits + "1010"; break;
                case "SRA": bits = bits + "1000"; break;
                case "RR": bits = bits + "1100"; break;
                case "SL0": bits = bits + "0110"; break;
                case "SL1": bits = bits + "0111"; break;
                case "SLX": bits = bits + "0010"; break;
                case "SLA": bits = bits + "0000"; break;
                case "RL": bits = bits + "0100"; break;
                default: throw new Erro(4);
            }
        }
    }

    private void handleArithData(int ist, String op1, String op2, int nu) throws Exception{
        if(argumentumNumberError(3,nu)){
            if(isRegister(op2)){
                bits = integerTostring(ist+1,6);
                bits = bits + integerTostring(getRegisterNumber(op1),4);
                bits = bits + integerTostring(getRegisterNumber(op2),4);
                bits = bits + integerTostring(0,4);
            }else{
                //if(isOperand2(op2))
                //if(op2.length()!=2) throw new Exception("ERROR: " + ist +" " + op1 +" " +op2);
                bits = integerTostring(ist,6);
                bits = bits + integerTostring(getRegisterNumber(op1),4);
                bits = bits + integerTostring(getOperand2(op2),8);
            }
        }
    }

    private void handleArithData2(int ist, String op1, String op2, int nu) throws Exception{
        if(argumentumNumberError(3,nu)){
            if(isRegister(op2)){
                bits = integerTostring(ist+1,6);
                bits = bits + integerTostring(getRegisterNumber(op1),4);
                bits = bits + integerTostring(getRegisterNumber(op2),4);
                bits = bits + integerTostring(0,4);
            }else{
                if(op2.length()!=2) throw new Erro(5);
                bits = integerTostring(ist,6);
                bits = bits + integerTostring(getRegisterNumber(op1),4);
                bits = bits + integerTostring(0,2);
                bits = bits + integerTostring(getOperand2(op2),6);
            }
        }
    }

    private boolean isRegister(String op) throws Exception{
        if(op != null && PicoBlazeCompillator.nameregs.containsKey(op)){
            return true;
        }
        if(op != null && PicoBlazeCompillator.nameregs.containsValue(op.toUpperCase())) throw new Erro(6);
        try {
            if (op.length() == 2 && op.toUpperCase().charAt(0) == 'S' && Integer.parseInt(op.charAt(1) + "") >= 0 && Integer.parseInt(op.charAt(1) + "") < 16)
                return true;
        }catch (Exception ex){
            throw new Erro(5);
        }
        return false;
    }

    private boolean isOperand2(String op){
        if(op != null && PicoBlazeCompillator.constants.containsKey(op)){
            return true;
        }
        return false;
    }

    private int getOperand2(String op) throws Exception {
        if(PicoBlazeCompillator.constants.containsKey(op)){
            return Integer.parseInt(PicoBlazeCompillator.constants.get(op),16);
        }
        if(op.length() != 2) throw new Erro(5);
        try {
            return Integer.parseInt(op + "", 16);
        }catch (Exception ex){
            throw new Erro(5);
        }

    }

    private int getRegisterNumber(String op) throws Exception {
        if(isRegister(op) && PicoBlazeCompillator.nameregs.containsKey(op)){
            return Integer.parseInt(PicoBlazeCompillator.nameregs.get(op).charAt(1) + "",16);
        }
        if(op.length() > 2) throw new Erro(4);
        if(op.toUpperCase().charAt(0) != 'S') throw new Erro(5);
        return Integer.parseInt(op.charAt(1) + "", 16);
    }

    private String integerTostring(int x, int n){
        return String.format("%"+n+"s", Integer.toBinaryString(x)).replace(' ', '0');
    }

    public int getInstructionInt(){
        return Integer.parseInt(bits,2);
    }

    private boolean argumentumNumberError(int needed, int get) throws Exception {
        if(needed ==  get) return true;
        if(needed > get){
            throw new Erro(1);
        }else{
            throw new Erro(2);
        }
    }

}
