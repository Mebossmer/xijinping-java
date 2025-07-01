package org.xijinping.bot.time;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.xijinping.bot.Bot;
import org.xijinping.bot.savedata.SaveFile;
import org.xijinping.bot.util.EmbedHelper;
import org.xijinping.bot.util.TimeHelper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Timer {
    long messageId; // 0, if message was not sent yet
    Date startTime;
    long targetUserId;
    long startedByUserId;
    VoiceChannel targetVoiceChannel;
    TextChannel textChannel; // where the timer was started

    boolean active;

    public Timer(long targetUserId, long startedByUserId, VoiceChannel targetVoiceChannel, TextChannel textChannel) {
        this.targetUserId = targetUserId;
        this.startedByUserId = startedByUserId;
        this.targetVoiceChannel = targetVoiceChannel;
        this.startTime = new Date();
        this.textChannel = textChannel;
        this.active = true;

        this.messageId = 0;
    }

    // evaluates and removes the amount of social credits accumulated
    public void stopAndEvaluate() {
        this.active = false;

        long timeInMilliseconds = new Date().getTime() - startTime.getTime();

        long timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds);

        long socialCreditsAmount = -Bot.getConfig().lateMultiplier * timeInSeconds;

        long currentSocialCredits = SaveFile.retrieve().addSocialCredits(targetUserId, socialCreditsAmount);

        boolean isRecord = SaveFile.retrieve().setRecordIfRecord(targetUserId, timeInMilliseconds);

        EmbedBuilder builder = EmbedHelper.createSocialCreditsEmbed(
            socialCreditsAmount, 
            Bot.getDiscord().getUserById(targetUserId), 
            currentSocialCredits, 
            (isRecord ? "NEW RECORD: " : "") + "You were " + TimeHelper.getTimeAsString(timeInMilliseconds) + " too late!!!");

        textChannel.sendMessageEmbeds(builder.build()).queue();
    }

    public EmbedBuilder getMessageEmbed() {
        User targetUser = Bot.getDiscord().getUserById(targetUserId);

        String text = TimeHelper.getTimeDifferenceAsString(startTime, new Date());

        if(active) {
            return new EmbedBuilder()
                .setColor(0xFF0000)
                .setTitle("⏲️ YOU ARE LATE ⏲️")
                .setDescription(
                    targetUser.getAsMention() + "is late. Quickly join " + targetVoiceChannel.getAsMention() + "!!!")
                .addField("Time", text, false)
                .setTimestamp(Instant.now());
        } else {
            return new EmbedBuilder()
                .setColor(0xFF0000)
                .setTitle("⏲️ YOU ARE LATE ⏲️")
                .setDescription("Timer stopped")
                .setTimestamp(Instant.now());
        }
    }

    public void updateMessage() {
        if(messageId == 0) {
            textChannel.sendMessageEmbeds(getMessageEmbed().build())
                .addActionRow(Button.primary("btn_timer_stop", "Stop"))
                .queue(msg -> this.messageId = msg.getIdLong());
        }

        textChannel.retrieveMessageById(messageId).queue(msg -> {
            msg.editMessageEmbeds(getMessageEmbed().build()).queue();

            if(!active) {
                msg.editMessageComponents().queue();
            }
        });
    }

    public long getMessageId() {
        return messageId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public long getTargetUserId() {
        return targetUserId;
    }

    public long getStartedByUserId() {
        return startedByUserId;
    }

    public VoiceChannel getTargetVoiceChannel() {
        return targetVoiceChannel;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean value) {
        this.active = value;
    }
}
