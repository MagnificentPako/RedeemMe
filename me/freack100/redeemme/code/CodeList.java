/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.redeemme.code;

import java.util.ArrayList;
import java.util.List;

public class CodeList {

    private List<Code> list;

    public CodeList(){
        list = new ArrayList<Code>();
    }

    public void add(Code code){
        list.add(code);
    }

    public void add(String code, String type, CodeType mode){
        list.add(new Code(code,type,mode));
    }

    public boolean contains(Code code){
        return list.contains(code);
    }

    public boolean containsCode(String code){
        for(Code c : list){
            if(c.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }

    public void remove(Code code){
        list.remove(code);
    }

    public void removeCode(String code){
        Code code_ = null;
        for(Code c : list){
            if(c.getCode().equals(code)){
                code_ = c;
            }
        }
        list.remove(code_);
    }

    public Code getByCode(String code){
        for(Code c : getCodes()){
            if(c.getCode().equals(code)){
                return c;
            }
        }
        return null;
    }

    public void clear(){
        list.clear();
    }

    public int size(){
        return list.size();
    }

    public List<Code> getCodes(){
        return list;
    }

}
