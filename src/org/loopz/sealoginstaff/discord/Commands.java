package org.loopz.sealoginstaff.discord;


import java.awt.*;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.loopz.sealoginstaff.Main;
import org.loopz.sealoginstaff.sqlite.SQLite;
import org.loopz.sealoginstaff.utils.Utils;

public class Commands extends ListenerAdapter {

    private final String pref;
    public Commands() {
        this.pref = this.Manager().getString("System.prefix") == null ? "s!" : this.Manager().getString("System.prefix");
    }
    public Utils Manager() {
        return Main.getPlugin(Main.class).utils;
    }
    public SQLite SQLite(){
        return Main.getPlugin(Main.class).connection;
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        MessageChannel mc = event.getChannel();
        Guild server = Main.getPlugin(Main.class).bot.jda.getGuildById(this.Manager().getString("Config.ServerID"));
        if (this.Manager().getString("Config.ChannelID") == null || this.Manager().getString("Config.ChannelID").equals("") || mc.getId().equals(this.Manager().getString("Config.ChannelID"))) {
            String[] cmd = event.getMessage().getContentRaw().split(" ");
            if (cmd[0].startsWith(pref)) {
                String discordid = event.getMember().getUser().getId();
                String discordnick = event.getMember().getAsMention();
                Role role = server.getRoleById(Main.getPlugin(Main.class).getConfig().getString("Config.RoleID"));
                if(!event.getMember().getRoles().contains(role)) {
                    embed.setColor(Color.decode("#FF0000"));
                    embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                    embed.setTitle(getMsg("not-staff"));
                    embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                    mc.sendMessage(embed.build()).complete();
                    return;
                }
                try {
                    switch (cmd[0].substring(pref.length())) {
                        case "vincular":
                        case "register":
                            if (cmd.length == 2) {
                                if (isValid(cmd[1])) {
                                    if (SQLite().getList(discordid).size() < this.Manager().getInt("System.accounts-limit")) {
                                        switch (SQLite().check(cmd[1], discordid)) {
                                            case 1:
                                                SQLite().register(cmd[1], discordid);
                                                embed.setColor(Color.decode("#00ff00"));
                                                embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                                embed.setTitle(getMsg("register-sucess"));
                                                embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                                mc.sendMessage(embed.build()).complete();
                                                break;
                                            case 2:
                                                embed.setColor(Color.decode("#FF0000"));
                                                embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                                embed.setTitle(getMsg("already-registred"));
                                                embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                                mc.sendMessage(embed.build()).complete();
                                                break;
                                            case 3:
                                                embed.setColor(Color.decode("#FF0000"));
                                                embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                                embed.setTitle(getMsg("registred-already"));
                                                embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                                mc.sendMessage(embed.build()).complete();
                                                break;
                                            default:
                                                mc.sendMessage("Error code 04, contact support seastore.").complete();
                                        }
                                        break;
                                    }
                                    embed.setColor(Color.decode("#FF0000"));
                                    embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                    embed.setTitle(getMsg("limit-reached"));
                                    embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                    mc.sendMessage(embed.build()).complete();
                                    break;
                                }
                                embed.setColor(Color.decode("#FF0000"));
                                embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                embed.setTitle(getMsg("invalid-nickname"));
                                embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                mc.sendMessage(embed.build()).complete();
                                break;
                            }
                            embed.setColor(Color.decode("#FF0000"));
                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                            embed.setTitle(getMsg("incomplete-command"));
                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                            mc.sendMessage(embed.build()).complete();
                                break;
                        case "desvincular":
                        case "unregister":
                            if (cmd.length == 2) {
                                if (isValid(cmd[1])) {
                                    switch (SQLite().check(cmd[1], discordid)) {
                                        case 1:
                                            embed.setColor(Color.decode("#FF0000"));
                                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                            embed.setTitle(getMsg("not-registred"));
                                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                            mc.sendMessage(embed.build()).complete();                                            break;
                                        case 2:
                                            embed.setColor(Color.decode("#FF0000"));
                                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                            embed.setTitle(getMsg("already-registred"));
                                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                            mc.sendMessage(embed.build()).complete();
                                            break;
                                        case 3:
                                            Player p = Bukkit.getServer().getPlayerExact(cmd[1]);
                                            if(p != null) {
                                                this.Manager().kickPlayer(cmd[1], "");
                                            }
                                            SQLite().unregister(cmd[1], discordid);
                                            embed.setColor(Color.decode("#00ff00"));
                                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                            embed.setTitle(getMsg("unregister"));
                                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                            mc.sendMessage(embed.build()).complete();
                                            break;
                                        default:
                                            mc.sendMessage("Error code 04, contact support seastore.").complete();
                                    }
                                    break;
                                }
                                embed.setColor(Color.decode("#FF0000"));
                                embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                embed.setTitle(getMsg("invalid-nickname"));
                                embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                mc.sendMessage(embed.build()).complete();
                                break;
                            }
                            embed.setColor(Color.decode("#FF0000"));
                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                            embed.setTitle(getMsg("incomplete-command"));
                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                            mc.sendMessage(embed.build()).complete();

                            break;
                        case "list":
                        case "accounts":
                            if (SQLite().getList(discordid).size() > 0) {
                                embed.setColor(Color.decode("#00ff00"));
                                embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                embed.setTitle(getMsg("accounts").replace("@accounts", convStringList(SQLite().getList(discordid))));
                                embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                mc.sendMessage(embed.build()).complete();
                                break;
                            }
                            embed.setColor(Color.decode("#FF0000"));
                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                            embed.setTitle( getMsg("empty-list"));
                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                            mc.sendMessage(embed.build()).complete();
                            break;
                        case "logar":
                        case "login":
                            if (cmd.length == 2) {
                                if (isValid(cmd[1])) {
                                    switch (SQLite().check(cmd[1], discordid)) {
                                        case 1:
                                            embed.setColor(Color.decode("#FF0000"));
                                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                            embed.setTitle(getMsg("not-registred"));
                                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                            mc.sendMessage(embed.build()).complete();                                            break;
                                        case 2:
                                            embed.setColor(Color.decode("#FF0000"));
                                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                            embed.setTitle(getMsg("already-registred"));
                                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                            mc.sendMessage(embed.build()).complete();                                    break;
                                        case 3:
                                            if(SQLite().checkLogged(cmd[1]) != 3) {
                                                SQLite().setLogged(cmd[1]);
                                                embed.setColor(Color.decode("#00ff00"));
                                                embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                                embed.setTitle(getMsg("login"));
                                                embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                                mc.sendMessage(embed.build()).complete();                                                break;
                                            }
                                            embed.setColor(Color.decode("#FF0000"));
                                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                            embed.setTitle(getMsg("already-logged"));
                                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                            mc.sendMessage(embed.build()).complete();                                            break;
                                        default:
                                            mc.sendMessage("Error code 04, contact support seastore.").complete();
                                            break;
                                    }
                                    break;
                                }
                                embed.setColor(Color.decode("#FF0000"));
                                embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                                embed.setTitle(getMsg("invalid-nickname"));
                                embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                                mc.sendMessage(embed.build()).complete();
                                break;
                            }
                            embed.setColor(Color.decode("#FF0000"));
                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                            embed.setTitle(getMsg("incomplete-command"));
                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                            mc.sendMessage(embed.build()).complete();
                            break;
                        case "ayuda":
                        case "ajuda":
                        case "help":
                            embed.setColor(Color.decode("#00ff00"));
                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                            embed.setTitle(getMsg("help"));
                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                            mc.sendMessage(embed.build()).complete();
                            break;
                        default:
                            embed.setColor(Color.decode("#FF0000"));
                            embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                            embed.setTitle(getMsg("invalid-command"));
                            embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                            mc.sendMessage(embed.build()).complete();
                            break;
                    }
                } catch (Exception e) {
                    embed.setColor(Color.decode("#FF0000"));
                    embed.setAuthor("LoginStaff - Security", "https://discord.gg/236yn5WtTm", server.getIconUrl());
                    embed.setTitle(getMsg("internal-error"));
                    embed.setFooter("SeaStore", "https://cdn.discordapp.com/icons/1003352708645912649/1befe00a9f1c0717de0c448358750a79");
                    mc.sendMessage(embed.build()).complete();
                        Bukkit.getConsoleSender().sendMessage("§c§lERROR: " + e.getMessage());
                }
            }
        }
    }

    private boolean isValid(String str) {
        return str.matches("(\\w{3,16})$");
    }

    private String convStringList(List<String> list) {
        StringBuilder msg = new StringBuilder(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            msg.append("\n").append(list.get(i));
        }
        return msg.toString();
    }
    private String getMsg(String msg) {
        return this.Manager().getMsg("Messages." + msg).replace("@prefix", pref);
    }

}
