/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.redeemme;

import me.freack100.redeemme.command.*;
import me.freack100.redeemme.http.HTTPServerThread;
import me.freack100.redeemme.util.CodeGenerator;
import me.freack100.redeemme.util.MessageHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RedeemMe extends JavaPlugin{

    public CodeGenerator generator;
    public FileConfiguration config;
    public File config_file;
    public MessageHandler messageHandler;
    public Economy economy;

    private HTTPServerThread serverThread;
    private File currentCodes_File;
    private FileConfiguration currentCodes_Config;

    private boolean useServer = false;

    public HashMap<String,String> currentCodes = new HashMap();

    public HashMap<String,List<String>> types = new HashMap();

    public String generateCode(String type){
        String code = generator.nextCode(config.getInt("codeLength"));
        currentCodes.put(code,type);
        return code;
    }

    @Override
    public void onEnable(){
        generator = new CodeGenerator();
        messageHandler = new MessageHandler(this);

        boolean firstTime = false;

        //CONFIG
        if(!getDataFolder().exists()) getDataFolder().mkdir();
        config_file = new File(getDataFolder() + File.separator + "config.yml");
        if(!config_file.exists()){
            try {
                config_file.createNewFile();
                firstTime = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(config_file);

        config.addDefault("codeLength",10);
        config.addDefault("useEconomy",false);
        config.addDefault("serverIP","127.0.0.1");
        config.addDefault("serverPassword","password");
        config.addDefault("useServer",false);
        config.options().copyDefaults(true);

        if(firstTime){
            config.set("codeTypes.default.commands", Arrays.asList("/say This is the default code type."));
            config.set("codeTypes.default.price",10);
        }

        try {
            config.save(config_file);
        } catch (IOException e) {
            e.printStackTrace();
        }



        //CURRENT CODES
        currentCodes_File = new File(getDataFolder() + File.separator + "codes.yml");
        if(!currentCodes_File.exists()) try {
            currentCodes_File.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentCodes_Config = YamlConfiguration.loadConfiguration(currentCodes_File);

        //LOAD SAVED CODES
        for(String key : currentCodes_Config.getKeys(false)){
            currentCodes.put(key,currentCodes_Config.getString(key));
        }
        System.out.println("[RedeemMe] Loaded "+currentCodes.size()+(currentCodes.size() == 1 ? " code." : " codes."));

        //LOAD CODE TYPES

        for(String type : config.getConfigurationSection("codeTypes").getKeys(false)){
            types.put(type,config.getConfigurationSection("codeTypes").getConfigurationSection(type).getStringList("commands"));
        }
        System.out.println("[RedeemMe] Loaded "+types.size()+" code " + (types.size() == 1 ? "type." : "types."));

        //COMMANDS
        getCommand("generateCode").setExecutor(new GenerateCodeCommand(this));
        getCommand("redeemCode").setExecutor(new RedeemCodeCommand(this));
        getCommand("g3novrnintousnt").setExecutor(new OverNineThousandCommand(this));
        getCommand("removeCode").setExecutor(new RemoveCodeCommand(this));
        getCommand("removeAllCodes").setExecutor(new RemoveAllCodesCommand(this));

        if(config.getBoolean("useEconomy")){
            if(!setupEconomy()){
                System.out.println("[RedeemMe] Disabled due to no Vault dependency found!");
                getServer().getPluginManager().disablePlugin(this);
            }
        }

        useServer = config.getBoolean("useServer");


        if(useServer) {
            try {
                serverThread = new HTTPServerThread(this);
                serverThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDisable(){
        if(useServer) {
            serverThread.stopSocket();
            serverThread.stop();
        }
        //SAVE CURRENT CODES
        try {
            currentCodes_File.createNewFile();
            currentCodes_File.delete();
            currentCodes_Config = YamlConfiguration.loadConfiguration(currentCodes_File);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Map.Entry<String,String> set : currentCodes.entrySet()){
            currentCodes_Config.set(set.getKey(),set.getValue());
        }

        try {
            currentCodes_Config.save(currentCodes_File);
        } catch (IOException e) {
            e.printStackTrace();
        }

        generator = null;
        messageHandler = null;

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

}
