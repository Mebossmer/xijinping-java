package org.xijinping.bot.command;

import java.time.Instant;
import java.util.Objects;

import org.xijinping.bot.savedata.SaveFile;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class LashCommand implements Command {

    @Override
    public void execute(SlashCommandInteraction intr) {
        User target = Objects.requireNonNull(intr.getOption("target")).getAsUser();

        // add a lash
        SaveFile.retrieve().addLash(target.getIdLong());

        // create message
        EmbedBuilder builder = new EmbedBuilder()
            .setColor(0xFF0000)
            .setTitle("🗣️ LAZY WORKER DETECTED 🗣️")
            .setDescription(target.getAsMention() + " should get back to work!!! +1 lash")
            .setImage("https://c.tenor.com/b4_5Oia2WD0AAAAC/tenor.gif")
            .setTimestamp(Instant.now());

        intr.replyEmbeds(builder.build()).queue();
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("lash", "Lash a user for not working hard enough")
            .addOption(OptionType.USER, "target", "The user to punish", true);
    }
}
