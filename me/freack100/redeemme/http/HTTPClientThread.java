/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.redeemme.http;

import me.freack100.redeemme.RedeemMe;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.StringTokenizer;

public class HTTPClientThread extends Thread {

    private RedeemMe plugin;
    private Socket socket;

    private BufferedReader inFromClient = null;
    private DataOutputStream outToClient = null;

    public HTTPClientThread(RedeemMe plugin,Socket socket){
        this.plugin = plugin;
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToClient = new DataOutputStream(socket.getOutputStream());

            String requestedString = inFromClient.readLine();
            String headerLine = requestedString;

            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();
            String httpQueryString = tokenizer.nextToken();

            StringBuffer responseBuffer = new StringBuffer();

            //System.out.println(httpMethod);
            if(httpMethod.equals("GET")){
                if(httpQueryString.equals("/")){
                    //responseBuffer.append("Nope");
                }else{
                    String text = httpQueryString.replaceFirst("/","");
                    if(text.contains("/")){
                        String[] get = text.split("/");
                        if(plugin.config.get("server.password").equals(get[0])){
                            if(plugin.types.containsKey(get[1])){
                                responseBuffer.append(plugin.generateCode(get[1]));
                            }
                        }
                    }
                }
            }

            sendResponse(200,responseBuffer.toString(),false);



        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void sendResponse (int statusCode, String responseString, boolean isFile) throws Exception {

        String statusLine = null;
        String serverdetails = "Server: Java HTTPServer";
        String contentLengthLine = null;
        String fileName = null;
        String contentTypeLine = "Content-Type: text/html" + "\r\n";
        FileInputStream fin = null;

        if (statusCode == 200)
            statusLine = "HTTP/1.1 200 OK" + "\r\n";
        else
            statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

        if (isFile) {
            fileName = responseString;
            fin = new FileInputStream(fileName);
            contentLengthLine = "Content-Length: " + Integer.toString(fin.available()) + "\r\n";
            if (!fileName.endsWith(".htm") && !fileName.endsWith(".html"))
                contentTypeLine = "Content-Type: \r\n";
        }
        else {
            contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
        }

        outToClient.writeBytes(statusLine);
        outToClient.writeBytes(serverdetails);
        outToClient.writeBytes(contentTypeLine);
        outToClient.writeBytes(contentLengthLine);
        outToClient.writeBytes("Connection: close\r\n");
        outToClient.writeBytes("\r\n");

        if (isFile) sendFile(fin, outToClient);
        else outToClient.writeBytes(responseString);

        outToClient.close();
    }

    public void sendFile (FileInputStream fin, DataOutputStream out) throws Exception {
        byte[] buffer = new byte[1024] ;
        int bytesRead;

        while ((bytesRead = fin.read(buffer)) != -1 ) {
            out.write(buffer, 0, bytesRead);
        }
        fin.close();
    }


}
