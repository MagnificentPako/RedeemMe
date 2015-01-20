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
import sun.nio.ch.ThreadPool;

import java.io.IOException;
import java.net.*;

public class HTTPServerThread extends Thread {

    private RedeemMe plugin;

    private int port;
    private ServerSocket socket;

    public HTTPServerThread(RedeemMe plugin) throws IOException {
        this.plugin = plugin;
        socket = new ServerSocket(plugin.config.getInt("server.port"),10, InetAddress.getByName(plugin.config.getString("server.ip")));
        System.out.println("Opened server on port "+plugin.config.getInt("server.port")+".");
    }

    @Override
    public void run() {
        while(true){
            try {
                Socket connected = socket.accept();
                (new HTTPClientThread(plugin,connected)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopSocket(){
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
