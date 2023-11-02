package org.loopz.sealoginstaff.sqlite;

import org.loopz.sealoginstaff.Main;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class SQLite {
    public Main plugin;
    public SQLite(Main plugin) {
        this.plugin = plugin;
    }
    private Connection con;
    public boolean isConnected() {
        return this.con != null;
    }

    public void openConnectionSQLITE() {
        try {
            File file = new File(this.plugin.getDataFolder(), "staffs.db");
            Class.forName("org.sqlite.JDBC");
            this.con = DriverManager.getConnection("jdbc:sqlite:" + file);
            Bukkit.getConsoleSender().sendMessage("§a[SeaLoginStaff] - Conexão com SQLite foi um sucesso.");
            createTable();
        } catch (Exception var2) {
            Bukkit.getConsoleSender().sendMessage("§c[SeaLoginStaff] - não foi possivel se conectar a o SQLite, desligando plugin.");
            this.plugin.getPluginLoader().disablePlugin(this.plugin);
        }

    }

    public void disconnect() {
        if (this.isConnected()) {
            try {
                this.con.close();
                Bukkit.getConsoleSender().sendMessage("§c[SeaLoginStaff] - Conexão finalizada com sucesso.");
            } catch (SQLException var2) {
                var2.printStackTrace();
            }
        }

    }
    public void createTable() {
        PreparedStatement stm = null;

        try {
            stm = this.con.prepareStatement("CREATE TABLE IF NOT EXISTS SeaLoginStaff(name varchar(16) not null unique, discordid varchar(18) not null, LOGGED varchar(16) default NULL, ban boolean default false)");
            stm.executeUpdate();
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
            try {
                stm.close();
            } catch (SQLException var10) {
                var10.printStackTrace();
            }

        }
    }
    public boolean contains(String NAME) {
        PreparedStatement stm = null;

        try {
            stm = this.con.prepareStatement("SELECT * FROM SeaLoginStaff WHERE NAME = ?");
            stm.setString(1, NAME);
            ResultSet rs = stm.executeQuery();
            boolean resultado = rs.next();
            boolean var5 = resultado;
            return var5;
        } catch (Exception var15) {
            var15.printStackTrace();
        } finally {
            try {
                stm.close();
            } catch (SQLException var14) {
                var14.printStackTrace();
            }

        }

        return false;
    }
    public int check(String name, String discordid) {
        PreparedStatement stm = null;
        try {
            stm = this.con.prepareStatement("SELECT * FROM SeaLoginStaff WHERE NAME = ?");
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                if (rs.getString("discordid").equals(discordid)) {
                    return 3;
                }
                return 2;
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        try {
            stm.close();
        } catch (SQLException var10) {
            var10.printStackTrace();
        }

    }
        return 0;
    }

    public void unregister(String name, String discordid) {
        PreparedStatement stm = null;
        try {
            stm = this.con.prepareStatement("DELETE FROM SeaLoginStaff WHERE NAME = ? AND discordid = ?");
            stm.setString(1, name);
            stm.setString(2, discordid);
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                stm.close();
            } catch (SQLException var10) {
                var10.printStackTrace();
            }

        }
    }
    public int checkLogged(String name) {
        PreparedStatement stm = null;
        try {
            stm = this.con.prepareStatement("SELECT * FROM SeaLoginStaff WHERE NAME = ?");
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                if (rs.getString("LOGGED") == null) {
                        return 2;
                }
                return 3;
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stm.close();
            } catch (SQLException var10) {
                var10.printStackTrace();
            }

        }
        return 1;
    }

    public void setLogged(String name) {
        PreparedStatement stm = null;
        try {
            stm = this.con.prepareStatement("UPDATE SeaLoginStaff SET LOGGED = ? WHERE NAME = ?");
            stm.setString(2, name);
            stm.setString(1, "yes");
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                stm.close();
            } catch (SQLException var10) {
                var10.printStackTrace();
            }
        }
    }
    public void resetLogged(String name) {
        PreparedStatement stm = null;
        try {
            stm = this.con.prepareStatement("UPDATE SeaLoginStaff SET LOGGED = NULL WHERE NAME = ?");
            stm.setString(1, name);
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                stm.close();
            } catch (SQLException var10) {
                var10.printStackTrace();
            }
        }
    }

    public String getDiscord(String name) {
        PreparedStatement stm = null;
        try {
            stm = this.con.prepareStatement("SELECT discordid FROM SeaLoginStaff WHERE NAME = ?");
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getString("discordid");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                stm.close();
            } catch (SQLException var10) {
                var10.printStackTrace();
            }

        }
        return null;
    }
    public void register(String name, String discordid) {
        PreparedStatement stm = null;
        try {
            stm = this.con.prepareStatement("INSERT INTO SeaLoginStaff (name, discordid) VALUES (?,?)");
            stm.setString(1, name);
            stm.setString(2, discordid);
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                stm.close();
            } catch (SQLException var10) {
                var10.printStackTrace();
            }
        }
    }

    public List<String> getList(String discordid) {
        PreparedStatement stm = null;
        try {
            stm = this.con.prepareStatement("SELECT name FROM SeaLoginStaff WHERE discordid=?");
            stm.setString(1, discordid);
            ResultSet rs = stm.executeQuery();
            List<String> ls = new ArrayList<>();
            while (rs.next()) {
                ls.add(rs.getString("name"));
            }
            return ls;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stm.close();
            } catch (SQLException var10) {
                var10.printStackTrace();
            }
        }
        return null;
    }
}
