/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.redeemme.file;

import java.io.*;

public class TextFile {

    private File file;

    public TextFile(File file){
        this.file = file;
        if(!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeContent(String content) throws IOException {
        Writer output = new BufferedWriter(new FileWriter(file));
        try{
            output.write(content);
            //output.flush();
        }finally {
            output.close();
        }
    }

    public String readContent(){
        StringBuilder contents = new StringBuilder();

        try{
            BufferedReader input = new BufferedReader(new FileReader(file));
            try{
                String line = null;
                while((line = input.readLine()) != null){
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents.toString();
    }

    public void appendContent(String content){
        String cont = readContent();
        try {
            writeContent(cont+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
