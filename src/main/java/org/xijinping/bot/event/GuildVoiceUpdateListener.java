package org.xijinping.bot.event;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.xijinping.bot.Bot;
import org.xijinping.bot.command.Timer;
import org.xijinping.bot.savedata.SaveFile;
import org.xijinping.bot.util.EmbedHelper;
import org.xijinping.bot.util.TimeHelper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildVoiceUpdateListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        // handle late records

        AudioChannelUnion union = event.getChannelJoined();
        if(union == null) {
            return;
        }

        VoiceChannel joined = union.asVoiceChannel();
        if(joined == null) {
            return;
        }

        User toStop = null;
        for(Timer t : Timer.timers) {
            if(t.channel().getId().equals(joined.getId())) {
                toStop = event.getMember().getUser();

                // Edit timer message
                EmbedBuilder builder = new EmbedBuilder();
                builder.setColor(0xFF0000);
                builder.setTitle("⏲️ YOU ARE LATE ⏲️");
                builder.setDescription("Timer stopped");
                builder.setTimestamp(Instant.now());

                t.textChannel().retrieveMessageById(t.messageId()).queue(msg -> {
                    msg.editMessageEmbeds(builder.build()).queue();
                    msg.editMessageComponents().queue();
                });

                // time between start of timer and now
                long timeInMilliseconds = new Date().getTime() - t.startTime().getTime();

                // calculate amount of social credits
                long amount = -Bot.getConfig().lateMultiplier * TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds);

                // remove social credits
                long currentSocialCredits = SaveFile.retrieve().addSocialCredits(event.getMember().getUser().getIdLong(), amount);

                // check if new record
                boolean isRecord = SaveFile.retrieve().setRecordIfRecord(event.getMember().getUser().getIdLong(), timeInMilliseconds);

                // send message
                EmbedBuilder builder2 = EmbedHelper.createSocialCreditsEmbed(
                    amount, t.target(), currentSocialCredits, 
                    (isRecord ? "NEW RECORD: " : "") + "You were " + TimeHelper.getTimeAsString(timeInMilliseconds) + " too late!!!");

                t.textChannel().sendMessageEmbeds(builder2.build()).queue();;

                break;
            }
        }

        Timer.stopTimer(toStop);
    }

}
