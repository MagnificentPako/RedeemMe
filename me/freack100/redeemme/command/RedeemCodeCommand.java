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
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class RedeemCodeCommand implements CommandExecutor {

    private RedeemMe plugin;

    public RedeemCodeCommand(RedeemMe plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(args.length != 1) return true;

        String code = args[0].toUpperCase();

        if(!(sender instanceof Player)) return true;
        if(args.length != 1) return true;
        if(!plugin.currentCodes.containsCode(code)) return true;

        Player player = (Player) sender;
        String type = plugin.currentCodes.getByCode(code).getCodeType();


        String msg = plugin.messageHandler.getMessage("redeem");
        player.sendMessage(msg
                .replace("%USERNAME%",player.getName())
                .replace("%CODE%",code)
                .replace("%TYPE%",type)
        );
        for(String command : plugin.types.get(type)){
            String theCommand = command.startsWith("/") ? command.substring(1) : command;
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                    theCommand
                            .replace("%USERNAME%",player.getName())
                            .replace("%TYPE%",type)
                            .replace("%CODE%",args[0])
            );
        }
        plugin.currentCodes.removeCode(code);
        return true;
    }

}
