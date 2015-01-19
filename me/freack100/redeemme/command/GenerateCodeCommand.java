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

public class GenerateCodeCommand implements CommandExecutor {

    private RedeemMe plugin;

    public GenerateCodeCommand(RedeemMe plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(!sender.hasPermission("redeemme.generate")) return true;
        if(args.length != 1) return true;
        if(!plugin.types.containsKey(args[0])) return true;

        sender.sendMessage("Here is your code of type "+args[0]+": "+plugin.generateCode(args[0]));

        return true;
    }

}
