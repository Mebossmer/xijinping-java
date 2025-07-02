package org.xijinping.bot.command;

import java.time.Instant;
import java.util.Optional;

import org.xijinping.bot.savedata.SaveFile;
import org.xijinping.bot.savedata.UserData;
import org.xijinping.bot.util.TimeHelper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class GetUserDataCommand extends Command {

    @Override
    public void execute(SlashCommandInteraction intr) {
        User target = intr.getOption("target").getAsUser();
        if(target == null) {
            return;
        }

        SaveFile file = SaveFile.retrieve();

        Optional<UserData> userData = file.stream().filter(ud -> target.getIdLong() == ud.userId).findFirst();
        if(!userData.isPresent()) {
            return;
        }

        EmbedBuilder builder = new EmbedBuilder()
            .setColor(0xFF0000)
            .setTitle("INFO")
            .setDescription(String.format("user data of %s", target.getAsMention()))
            .addField("Social Credits", String.valueOf(userData.get().socialCreditAmount), false)
            .addField("Lashes", String.valueOf(userData.get().lashes), false)
            .addField("Late Record", TimeHelper.getTimeAsString(userData.get().lateRecord), false)
            .setTimestamp(Instant.now());

        intr.replyEmbeds(builder.build())
            .setEphemeral(true)
            .queue();
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("getuserdata", "view user data")
            .addOption(OptionType.USER, "target", "the user", true);
    }
    
}
