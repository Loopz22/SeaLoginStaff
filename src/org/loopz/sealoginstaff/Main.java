package org.loopz.sealoginstaff;

import net.dv8tion.jda.core.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.loopz.sealoginstaff.discord.Bot;
import org.loopz.sealoginstaff.utils.Language;
import org.loopz.sealoginstaff.sqlite.SQLite;
import org.loopz.sealoginstaff.utils.Utils;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Filter;
public class Main extends JavaPlugin implements Listener {

    public Bot bot;
    public Language lang;
    public Utils utils;
    public SQLite connection;

    @Override
    public void onEnable() {
        this.lang = new Language();
        this.utils = new Utils();
        this.connection = new SQLite(this);
        saveDefaultConfig();
        this.connection.openConnectionSQLITE();
        start();
        this.lang.setupLanguage();
        Bukkit.getPluginManager().registerEvents(this, this);
        desativarConsole();
    }

    public void desativarConsole() {
        Filter f = lr -> {
            String msg = lr.getMessage();
            return (msg.contains("logged in with") || msg.contains("Disconnecting") || msg.contains("lost connection"));
        };
        Bukkit.getLogger().setFilter(f);
    }
    @Override
    public void onLoad(){
        if (bot != null) {
            bot.stop();
        }
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(p.hasPermission("SeaLoginStaff.admin")) {
            String nome = e.getPlayer().getName();
            if (this.connection.checkLogged(nome) == 3) {
                Guild g;
                if ((g = bot.jda.getGuildById(utils.getString("Config.ServerID"))) == null) {
                    g = bot.jda.getGuilds().get(0);
                }
                if (g.getMemberById(connection.getDiscord(nome)) == null) {
                    return;
                }
                this.connection.resetLogged(nome);
            }
        }
    }

    @EventHandler
    public void command(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if(p.hasPermission("SeaLoginStaff.admin")) {
            String nome = e.getPlayer().getName();
            switch (this.connection.checkLogged(nome)) {
                case 1:
                    p.sendMessage(utils.getMsg("Messages.no-register"));
                    e.setCancelled(true);
                    return;
                case 2:
                    p.sendMessage(utils.getMsg("Messages.no-logged"));
                    e.setCancelled(true);
                    return;
                case 3:
                    Guild g;
                    if ((g = bot.jda.getGuildById(utils.getString("Config.ServerID"))) == null) {
                        g = bot.jda.getGuilds().get(0);
                    }
                    if (g.getMemberById(connection.getDiscord(nome)) == null) {
                        p.sendMessage(utils.getMsg("Messages.no-in-discord"));
                        e.setCancelled(true);
                    }
                    p.sendMessage(utils.getMsg("Messages.logged-success"));
                    p.playSound(p.getLocation(), Sound.EXPLODE, 10f, 10f);
                    return;
            }
            p.kickPlayer("§cERROR CONTACT SUPPORT SEASTORE.");
        }
    }

    @EventHandler
    public void chat(PlayerChatEvent e) {
        Player p = e.getPlayer();
        if(p.hasPermission("SeaLoginStaff.admin")) {
            String nome = e.getPlayer().getName();
            switch (this.connection.checkLogged(nome)) {
                case 1:
                    p.sendMessage(utils.getMsg("Messages.no-register"));
                    e.setCancelled(true);
                    return;
                case 2:
                    p.sendMessage(utils.getMsg("Messages.no-logged"));
                    e.setCancelled(true);
                    return;
                case 3:
                    Guild g;
                    if ((g = bot.jda.getGuildById(utils.getString("Config.ServerID"))) == null) {
                        g = bot.jda.getGuilds().get(0);
                    }
                    if (g.getMemberById(connection.getDiscord(nome)) == null) {
                        p.sendMessage(utils.getMsg("Messages.no-in-discord"));
                        e.setCancelled(true);
                    }
                    p.sendMessage(utils.getMsg("Messages.logged-success"));
                    p.playSound(p.getLocation(), Sound.EXPLODE, 10f, 10f);
                    return;
            }
            p.kickPlayer("§cERROR CONTACT SUPPORT SEASTORE.");
        }
    }

    @EventHandler
    public void login(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(p.hasPermission("SeaLoginStaff.admin")) {
            String nome = e.getPlayer().getName();
            switch (this.connection.checkLogged(nome)) {
                case 1:
                    p.sendMessage(utils.getMsg("Messages.no-register"));
                    return;
                case 2:
                    p.sendMessage(utils.getMsg("Messages.no-logged"));
                return;
                case 3:
                    Guild g;
                    if ((g = bot.jda.getGuildById(utils.getString("Config.ServerID"))) == null) {
                        g = bot.jda.getGuilds().get(0);
                    }
                    if (g.getMemberById(connection.getDiscord(nome)) == null) {
                        p.sendMessage(utils.getMsg("Messages.no-in-discord"));
                    }
                    return;
            }
            p.kickPlayer("§cERROR CONTACT SUPPORT SEASTORE.");
        }
    }
    private static void wget(File to) throws IOException {
        if (to.exists() || to.mkdir()) {
            URL url = new URL("https://download1514.mediafire.com/veug4cb918ug/ut5sdejvxypv159/Discord-Bot-API-V3.jar");
            URLConnection urlc = url.openConnection();
            InputStream in = urlc.getInputStream();
            FileOutputStream f = new FileOutputStream(new File(to, url.getFile().split("/")[url.getFile().split("/").length - 1]));
            int i;
            byte[] b = new byte[1024];
            while ((i = in.read(b)) > 0) {
                f.write(b, 0, i);
            }
            f.close();
            in.close();
        }
    }
    private void start() {
        Bukkit.getConsoleSender().sendMessage("§a[SeaLoginStaff] - Verificando dependencias...");
        try {
            Class.forName("net.dv8tion.jda.core.JDABuilder");
        } catch (ClassNotFoundException ex) {
            Bukkit.getConsoleSender().sendMessage("§c[SeaLoginStaff] - Dependencias não encontrada.");
            Bukkit.getConsoleSender().sendMessage("§e[SeaLoginStaff] - Tentando baixar dependencias...");
        }
                try {
                    wget(new File("./plugins"));
                    Bukkit.getConsoleSender().sendMessage("§a[SeaLoginStaff] - Dependencias baixadas com sucesso.");
                } catch (IOException ex2) {
                    Bukkit.getConsoleSender().sendMessage("§c§lERROR: " + ex2.getMessage());
                    return;
                }

            Bukkit.getConsoleSender().sendMessage("§c[SeaLoginStaff] - Reiniciando o servidor...");
            Bukkit.getServer().shutdown();

        Bukkit.getConsoleSender().sendMessage("§a[SeaLoginStaff] - Carregando bot...");
        try {
            bot = new Bot();
        } catch (LoginException ex) {
            Bukkit.getConsoleSender().sendMessage("§c§lERROR: " + ex.getMessage());
        }
    }
}