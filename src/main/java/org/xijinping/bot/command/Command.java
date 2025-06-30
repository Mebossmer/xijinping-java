package org.xijinping.bot.command;

import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class Command {
    public abstract void execute(SlashCommandInteraction intr);

    public abstract SlashCommandData getData();
}
