package org.xijinping.bot;

import javax.naming.ConfigurationException;

import org.xijinping.bot.command.CommandRegistry;
import org.xijinping.bot.config.BotConfig;
import org.xijinping.bot.event.ButtonInteractionEventListener;
import org.xijinping.bot.event.GuildVoiceUpdateListener;
import org.xijinping.bot.event.MessageReceiveEventListener;
import org.xijinping.bot.time.TimerManager;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {
    private static TimerManager timerManager = null;
    private static BotConfig config = null;
    private static JDA jda = null;

    public static void main(String[] args) throws Exception {
        // Load bot configuration file
        config = BotConfig.get("config.json");

        if(config.token.isEmpty()) {
            throw new ConfigurationException("Please provide your bot token in config.json");
        }

        timerManager = new TimerManager();

        // Register all commands
        CommandRegistry commands = new CommandRegistry();
        commands.register();

        // Connect to discord
        jda = JDABuilder.create(config.token,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(
                    commands, 
                    new MessageReceiveEventListener(),
                    new GuildVoiceUpdateListener(),
                    new ButtonInteractionEventListener())
                .build();

        // Finally, deploy all commands
        commands.deploy(jda);
    }

    public static BotConfig getConfig() {
        return config;
    }

    public static JDA getDiscord() {
        return jda;
    }

    public static TimerManager getTimerManager() {
        return timerManager;
    }

    public static JDA getJda() {
        return jda;
    }
}
