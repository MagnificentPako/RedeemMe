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
        config.addDefault("remove", "You removed a code.");
        config.addDefault("removeAll", "You removed all codes.");
        config.addDefault("noPermission", "You don't have the permissions to do this.");

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


}
