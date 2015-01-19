/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

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
