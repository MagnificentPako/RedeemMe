package me.freack100.redeemme.util;

import java.security.SecureRandom;

public class CodeGenerator {

    private SecureRandom random;
    private String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public CodeGenerator(){
        this.random = new SecureRandom();
    }

    public String nextCode(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(random.nextInt(AB.length())));
        return sb.toString();
    }

}
