package org.xijinping.bot.command;

import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface Command {
    void execute(SlashCommandInteraction intr);

    SlashCommandData getData();
}
