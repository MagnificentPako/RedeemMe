/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.redeemme.mysql;

import java.sql.Connection;

public class MySQL {

    private String ip;
    private int port;
    private String table;
    private String username;
    private String password;

    private Connection connection;

    public MySQL(){
        //Class.forName("");
    }

}
