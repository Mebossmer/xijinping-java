package org.xijinping.bot.command;

import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class StartTimerCommand extends Command {

    @Override
    public void execute(SlashCommandInteraction intr) {
        User target = intr.getOption("target").getAsUser();
        if(target == null) {
            return;
        }

        VoiceChannel ch = intr.getOption("channel").getAsChannel().asVoiceChannel();
        if(ch == null) {
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(0xFF0000);
        builder.setTitle("⏲️ YOU ARE LATE ⏲️");
        builder.setDescription(target.getAsMention() + " is late. Quickly join " + ch.getAsMention() + "!!!");
        builder.addField(new Field("Time", "Starting...", true));
        builder.setTimestamp(Instant.now());

        intr.replyEmbeds(builder.build())
            .addActionRow(Button.primary("btn_timer_stop", "Stop"))
            .queue(hook -> {
                hook.retrieveOriginal().queue(msg -> {
                    Timer.startTimer(msg.getId(), intr.getChannel().asTextChannel(), ch, target);
                });
            });
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("starttimer", "Start a timer for being too late")
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS))
            .addOption(OptionType.USER, "target", "the user to punish for being late", true)
            .addOption(OptionType.CHANNEL, "channel", "the channel to start the timer for", true);
    }
}
