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

        String code = args[0].toUpperCase();

        if(!(sender instanceof Player)) return true;
        if(args.length != 1) return true;
        if(!plugin.currentCodes.containsKey(code)) return true;

        Player player = (Player) sender;
        String type = plugin.currentCodes.get(code);


        player.sendMessage("You redeemed a "+type+" code.");
        for(String command : plugin.types.get(type)){
            String theCommand = command.startsWith("/") ? command.substring(1) : command;
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                    theCommand
                            .replace("%USERNAME%",player.getName())
                            .replace("%TYPE%",type)
                            .replace("%CODE%",args[0])
            );
        }
        plugin.currentCodes.remove(code);
        return true;
    }

}
