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
import me.freack100.redeemme.code.CodeType;
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
        //if(plugin.currentCodes.getByCode(code).getMode().equals(CodeType.PROMO)) sender.sendMessage("Promo codes are not yet supported.");

        Player player = (Player) sender;

        String type = plugin.currentCodes.getByCode(code).getCodeType();
        CodeType mode = plugin.currentCodes.getByCode(code).getMode();

        if(mode.equals(CodeType.NORMAL)) {
            player.sendMessage(plugin.messageHandler.formatMessage("redeem",
                    player.getName(),
                    code,
                    type));
            for (String command : plugin.types.get(type)) {
                String theCommand = command.startsWith("/") ? command.substring(1) : command;
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                        theCommand
                                .replace("%USERNAME%", player.getName())
                                .replace("%TYPE%", type)
                                .replace("%CODE%", args[0])
                );
            }
            plugin.currentCodes.removeCode(code);
            return true;
        }
        else if(mode.equals(CodeType.PROMO)){
            if(plugin.promoManager.redeemPromo(plugin.currentCodes.getByCode(code),player.getUniqueId())){
                player.sendMessage(plugin.messageHandler.formatMessage("redeemPromo",
                        player.getName(),
                        code,
                        type));
                for (String command : plugin.types.get(type)) {
                    String theCommand = command.startsWith("/") ? command.substring(1) : command;
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                            theCommand
                                    .replace("%USERNAME%", player.getName())
                                    .replace("%TYPE%", type)
                                    .replace("%CODE%", args[0])
                    );
                }
            }else{
                player.sendMessage(plugin.messageHandler.formatMessage("usedPromo",
                        player.getName(),
                        code,
                        type));
            }
            return true;
        }
        return true;
    }

}
