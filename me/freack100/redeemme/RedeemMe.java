/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.redeemme;

import me.freack100.redeemme.code.*;
import me.freack100.redeemme.command.*;
import me.freack100.redeemme.file.TextFile;
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
    private TextFile currentCodes_text;

    private boolean useServer = false;

    public CodeList currentCodes;
    public PromoManager promoManager;

    public HashMap<String,List<String>> types = new HashMap();

    public String generateCode(String type,CodeType mode){
        String code_ = generator.nextCode(config.getInt("codeLength"));
        Code code = new Code(code_,type,mode);
        currentCodes.add(code);
        return code_;
    }

    public String generateCode(String type){
        String code_ = generator.nextCode(config.getInt("codeLength"));
        Code code = new Code(code_,type,CodeType.NORMAL);
        currentCodes.add(code);
        return code_;
    }

    @Override
    public void onEnable(){
        currentCodes = new CodeList();
        generator = new CodeGenerator();
        messageHandler = new MessageHandler(this);
        promoManager = new PromoManager(this);

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
        config.addDefault("useServer",false);
        config.addDefault("server.ip","127.0.0.1");
        config.addDefault("server.password","password");
        config.addDefault("server.port",1337);

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
        currentCodes_File = new File(getDataFolder() + File.separator + "codes.dat");
        if(!currentCodes_File.exists()) try {
            currentCodes_File.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentCodes_text = new TextFile(currentCodes_File);
        for(String str : currentCodes_text.readContent().split(System.getProperty("line.separator"))){
            currentCodes.add(Code.unSerialize(str));
        }

        System.out.println("[RedeemMe] Loaded "+currentCodes.size()+(currentCodes.size() == 1 ? " code." : " codes."));

        //LOAD CODE TYPES

        for(String type : config.getConfigurationSection("codeTypes").getKeys(false)){
            types.put(type,config.getConfigurationSection("codeTypes").getConfigurationSection(type).getStringList("commands"));
        }
        System.out.println("[RedeemMe] Loaded "+types.size()+" code " + (types.size() == 1 ? "type." : "types."));

        promoManager.enable();

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
            currentCodes_text.writeContent("");
            for(Code c : this.currentCodes.getCodes()){
                if(c != null) {
                    currentCodes_text.appendContent(c.serialize());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        promoManager.disable();

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
