/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.redeemme.util;

import me.freack100.redeemme.RedeemMe;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageHandler {

    private File config_file;
    private FileConfiguration config;
    private RedeemMe plugin;

    public MessageHandler(RedeemMe plugin) {
        this.plugin = plugin;
        config_file = new File(plugin.getDataFolder() + File.separator + "lang.yml");
        if (!config_file.exists()) try {
            config_file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        config = YamlConfiguration.loadConfiguration(config_file);

        config.addDefault("generate", "Here is your code: %CODE%");
        config.addDefault("redeem", "You redeemed a code.");
        config.addDefault("redeemPromo", "You redeemed a promo code.");
        config.addDefault("usedPromo", "You used that promo code allready.");
        config.addDefault("remove", "You removed a code.");
        config.addDefault("removeAll", "You removed all codes.");
        config.addDefault("noPermission", "You don't have the permissions to do this.");
        config.addDefault("paid", "You paid for the generation of this code.");
        config.addDefault("noMoney", "You don't have enough money to generate this code.");


        config.options().copyDefaults(true);
        try {
            config.save(config_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(key));
    }

    public String formatMessage(String key, String name, String type, String code){
        String msg = getMessage(key);
        if(name!=null){
            msg = msg.replace("%USERNAME%",name);
        }
        if(type!=null){
            msg = msg.replace("%TYPE%",type);
        }
        if(code!=null){
            msg = msg.replace("%CODE%",code);
        }
        return msg;
    }

}
