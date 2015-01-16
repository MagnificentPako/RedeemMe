package me.freack100.redeemme;

import me.freack100.redeemme.command.*;
import me.freack100.redeemme.util.CodeGenerator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RedeemMe extends JavaPlugin{

    public CodeGenerator generator;
    public FileConfiguration config;
    public File config_file;

    private File currentCodes_File;
    private FileConfiguration currentCodes_Config;

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

        //CONFIG
        if(!getDataFolder().exists()) getDataFolder().mkdir();
        config_file = new File(getDataFolder() + File.separator + "config.yml");
        if(!config_file.exists()){
            try {
                config_file.createNewFile();
                config.save(config_file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(config_file);
        config.addDefault("codeLength",10);
        config.options().copyDefaults(true);

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

    }

    @Override
    public void onDisable(){
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
            try {
                currentCodes_Config.save(currentCodes_File);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
