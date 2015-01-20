/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.redeemme.code;

public class Code {

    private String code;
    private String codeType;
    private CodeType mode;

    public Code(String code, String codeType, CodeType mode){
        this.code = code;
        this.codeType = codeType;
        this.mode = mode;
    }

    public String getCode() {
        return code;
    }

    public String getCodeType() {
        return codeType;
    }

    public CodeType getMode() {
        return mode;
    }

    public String serialize(){
        String serialized = "";
        serialized += this.code + ";";
        serialized += this.codeType + ";";
        serialized += this.mode.toString() + ";";
        return serialized;
    }

    public static Code unSerialize(String serialized){
        if(serialized.contains(";")) {
            String[] splitted = serialized.split(";");
            Code code = new Code(splitted[0], splitted[1], CodeType.valueOf(splitted[2]));
            return code;
        }
        return null;
    }

}
