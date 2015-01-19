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

        boolean execute = true;

        if(!(sender.hasPermission("redeemme.generate.*")) || !(sender.hasPermission("redeemme.generate."+args[0]))){execute = false;}
        if(args.length != 1) execute = false;
        if(!plugin.types.containsKey(args[0])) execute = false;

        if(!execute){
            sender.sendMessage(plugin.messageHandler.getMessage("noPermission"));
            return true;
        }

        String message = plugin.messageHandler.getMessage("generate");
        sender.sendMessage(message
                .replace("%USERNAME%",sender.getName())
                .replace("%TYPE%",args[0])
                .replace("%CODE%",plugin.generateCode(args[0]))
        );

        return true;
    }

}
