package org.loopz.sealoginstaff.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.loopz.sealoginstaff.Main;

import java.sql.SQLException;
import java.util.List;

public class Utils {
    public FileConfiguration conf;
    
    public String getMsg(String str) {
        this.conf = Main.getPlugin(Main.class).lang.getLang();
        List<String> lstr = conf.getStringList(str);
        StringBuilder msg;
        if (lstr == null || lstr.isEmpty()) {
            msg = new StringBuilder(conf.getString(str));
            if (msg != null) {
                msg = new StringBuilder(msg.toString().replace("&", "§"));
            }
        } else {
            msg = new StringBuilder(lstr.get(0).replace("&", "§"));
            for (int i = 1; i < lstr.size(); i++) {
                msg.append("\n").append(lstr.get(i).replace("&", "§"));
            }
        }
        return (msg == null || msg.toString().equals("")) ? "NULL" : msg.toString();
    }

    
    public String getString(String str) {
        return Main.getPlugin(Main.class).getConfig().getString(str);
    }

    
    public int getInt(String str) {
        return Main.getPlugin(Main.class).getConfig().getInt(str);
    }



    
    public void kickPlayer(String nick, String motivo) {
        Player pp;
        if ((pp = Bukkit.getPlayer(nick)) != null) {
            pp.kickPlayer(motivo);
        }

    }
}
