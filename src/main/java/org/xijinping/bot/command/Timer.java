package org.xijinping.bot.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xijinping.bot.util.TimeHelper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public record Timer(
    String messageId,
    TextChannel textChannel,
    Date startTime,
    User target,
    VoiceChannel channel) {

    public static List<Timer> timers = new ArrayList<>();

    public static void startTimer(String messageId, TextChannel textChannel, VoiceChannel channel, User user) {
        timers.add(new Timer(
            messageId, 
            textChannel,
            new Date(), 
            user, 
            channel));
    }

    public static void stopTimer(String messageId) {
        Timer toRemove = null;
        for(Timer t : timers) {
            if(t.messageId.equals(messageId)) {
                toRemove = t;
            }
        }

        timers.remove(toRemove);
    }

    public static void stopTimer(User target) {
        Timer toRemove = null;
        for(Timer t : timers) {
            if(t.target.equals(target)) {
                toRemove = t;
            }
        }

        timers.remove(toRemove);
    }

    public static void updateTimers() {
        for(Timer t : timers) {
            t.textChannel.retrieveMessageById(t.messageId).queue(msg -> {
                EmbedBuilder builder = new EmbedBuilder(msg.getEmbeds().get(0));
                builder.clearFields();
                builder.addField(new Field("Time", TimeHelper.getTimeDifferenceAsString(t.startTime, new Date()), true));
                
                t.textChannel.editMessageEmbedsById(t.messageId, builder.build()).queue();
            });
        }
    }

    public static Timer getFromMessageId(long id) {
        for(Timer t : timers)  {
            if(t.messageId == String.valueOf(id)) {
                return t;
            }
        }

        return null;
    }
}
