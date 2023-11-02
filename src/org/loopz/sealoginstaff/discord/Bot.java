package org.loopz.sealoginstaff.discord;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.loopz.sealoginstaff.Main;

public class Bot {

    public JDA jda;

    public Bot() throws LoginException {
        JDABuilder jdab = new JDABuilder((Main.getPlugin(Main.class).utils.getString("Config.Token")));
        jdab.setGame(Game.listening(Main.getPlugin(Main.class).utils.getString("System.status")));
        jdab.addEventListener(new Commands());
        jdab.setAutoReconnect(false);
        jda = jdab.build();
    }

    public void stop() {
        if (jda != null) {
            jda.shutdown();
        }
    }
}
