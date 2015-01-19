/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.redeemme.command;

import me.freack100.redeemme.RedeemMe;
import org.bukkit.command.*;

public class OverNineThousandCommand implements CommandExecutor {

    private RedeemMe plugin;

    public OverNineThousandCommand(RedeemMe plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(!sender.hasPermission("redeemme.g3novrnintousnt")) return true;
        if(args.length != 1) return true;
        if(!plugin.types.containsKey(args[0])) return true;

        for(int i = 1; i < 9000; i++){
            plugin.generateCode(args[0]);
        }

        sender.sendMessage("YOU REALLY DID IT! YOU GENERATED OVER 9000 CODES!");

        return true;
    }

}
