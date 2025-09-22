package org.xijinping.bot.command;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import org.xijinping.bot.Bot;
import org.xijinping.bot.savedata.SaveFile;
import org.xijinping.bot.savedata.UserData;
import org.xijinping.bot.util.TimeHelper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class LeaderBoardCommand implements Command {

    @Override
    public void execute(SlashCommandInteraction intr) {
        int option = Objects.requireNonNull(intr.getOption("option")).getAsInt();

        // sort all members according to their "achievment"
        String title = "";

        SaveFile file = SaveFile.retrieve();
        switch(option) {
        case 1:
            title = "Our most loyal members ✅";
            file.sort(Comparator.comparingLong(u -> u.socialCreditAmount));
            break;
        case 2:
            title = "Our laziest workers 👎";
            file.sort(Comparator.comparingLong(u -> u.lashes));
            break;
        case 3:
            title = "Records for being late ⏰";
            file.sort(Comparator.comparingLong(u -> u.lateRecord));
            break;
        default:
            break;
        }
        Collections.reverse(file);

        // create message
        EmbedBuilder builder = new EmbedBuilder()
            .setColor(0xFF0000)
            .setTitle("LEADERBOARD")
            .setDescription(title)
            .setTimestamp(Instant.now());

        // add fields
        int i = 0;
        for(UserData data : file) {
            String text = "";
            switch(option) {
            case 1:
                text = String.valueOf(data.socialCreditAmount);
                break;
            case 2:
                text = String.valueOf(data.lashes);
                break;
            case 3:
                text = TimeHelper.getTimeAsString(data.lateRecord);
                break;
            default:
                break;
            }

            User usr = Bot.getDiscord().getUserById(data.userId);

            builder.addField(usr.getName(), text, false);
            
            // only display the first 20 entries
            if(i >= 20) {
                break;
            }

            i++;
        }
    
        intr.replyEmbeds(builder.build()).queue();;
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("leaderboard", "publicly shame lazy workers")
            .addOptions(new OptionData(OptionType.INTEGER, "option", "the value to rank", true)
                .addChoice("Social Credits", 1)
                .addChoice("Lashes", 2)
                .addChoice("Being Late", 3));
    }
}
