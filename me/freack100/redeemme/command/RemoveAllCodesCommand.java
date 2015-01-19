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

public class RemoveAllCodesCommand implements CommandExecutor {

    private RedeemMe plugin;

    public RemoveAllCodesCommand(RedeemMe plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(!sender.hasPermission("redeemme.removeAll")) return true;

        plugin.currentCodes.clear();
        String msg = plugin.messageHandler.getMessage("removeAll");
        sender.sendMessage(msg
                .replace("%USERNAME%",sender.getName())
        );

        return true;
    }

}
