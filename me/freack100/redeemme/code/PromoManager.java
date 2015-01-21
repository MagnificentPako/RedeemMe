/*
 *
 *  * Copyright © 2014-2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.redeemme.code;

import me.freack100.redeemme.RedeemMe;
import me.freack100.redeemme.file.TextFile;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PromoManager {

    private RedeemMe plugin;
    private TextFile promoFile;

    private HashMap<Code, List<UUID>> redeemed = new HashMap();

    public PromoManager(RedeemMe plugin) {
        this.plugin = plugin;
    }

    public boolean redeemPromo(Code code, UUID uuid) {
        if (!redeemed.containsKey(code)) redeemed.put(code, new ArrayList<UUID>());
        if (code.getMode() != CodeType.PROMO) return false;
        if (redeemed.get(code).contains(uuid)) return false;

        redeemed.get(code).add(uuid);
        return true;
    }

    public void enable() {
        //Load all the promo codes.
        promoFile = new TextFile(new File(plugin.getDataFolder() + File.separator + "promos.dat"));
        String[] lines = promoFile.readContent().split(System.getProperty("line.separator"));
        if (lines.length != 0) {
            for (String line : lines) {
                if (line.contains(":")) {
                    String[] firstSplit = line.split(":");
                    String code = firstSplit[0];
                    String uuids_ = firstSplit[1];
                    redeemed.put(plugin.currentCodes.getByCode(code), new ArrayList<UUID>());
                    String[] uuids = uuids_.split(";");
                    //System.out.println(uuids_);
                    //System.out.println(uuids.length);
                    for (String uuid : uuids) {
                        //System.out.println(uuid);
                        redeemed.get(plugin.currentCodes.getByCode(code)).add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getUniqueId());
                    }
                }
            }
        }
    }

    public void disable() {
        //Save them...
        try {
            promoFile.writeContent("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Code, List<UUID>> entry : redeemed.entrySet()) {
            String line = "";
            line += entry.getKey().getCode() + ":";
            for (UUID uuid : entry.getValue()) {
                line += uuid.toString() + ";";
            }
            //line = line.substring(0, line.length() - 1);
            promoFile.appendContent(line);
        }
    }

}
