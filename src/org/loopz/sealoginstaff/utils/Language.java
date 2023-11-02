package org.loopz.sealoginstaff.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.loopz.sealoginstaff.Main;
import org.loopz.sealoginstaff.sqlite.SQLite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Language {

    private File langFile;
    private Main instance = Main.getPlugin(Main.class);
    private FileConfiguration langCfg;

    public SQLite Manager(){
        return Main.getPlugin(Main.class).connection;
    }
    public void setupLanguage() {
            langFile = new File(instance.getDataFolder() + File.separator + "lang" + File.separator + instance.getConfig().getString("System.language") + ".yml");
        if(!langFile.exists()){
            instance.getDataFolder().mkdirs();
            langFile.getParentFile().mkdirs();
            try{
                langFile.createNewFile();
            }catch(IOException e){
                Bukkit.getConsoleSender().sendMessage("§c[SeaLoginStaff] - Ocorreu um erro ao criar arquivo de linguagens.");
                Bukkit.getPluginManager().disablePlugin(instance);
                return;
            }
            try{
                    InputStream jarUrl = getClass().getResourceAsStream("/" + instance.getConfig().getString("System.language") + ".yml");
                    Files.copy(jarUrl, langFile.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            }catch(Exception e){
                Bukkit.getConsoleSender().sendMessage("§c[SeaLoginStaff] - Ocorreu um erro ao identificar os arquivos de linguagens.");
                return;
            }
        }
        langCfg = YamlConfiguration.loadConfiguration(langFile);
    }
    public FileConfiguration getLang() {
        return langCfg;
    }

}
