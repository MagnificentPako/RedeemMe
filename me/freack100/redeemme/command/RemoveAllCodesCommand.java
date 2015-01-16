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
        sender.sendMessage("You removed all codes successful.");

        return true;
    }

}
