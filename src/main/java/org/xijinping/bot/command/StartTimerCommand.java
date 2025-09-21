package org.xijinping.bot.command;

import org.xijinping.bot.Bot;
import org.xijinping.bot.time.Timer;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class StartTimerCommand implements Command {

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

        TextChannel txch = intr.getChannel().asTextChannel();

        // check if a timer already exists for the target user
        for(Timer t : Bot.getTimerManager().getTimers()) {
            if(t.getTargetUserId() == target.getIdLong()) {
                intr.reply("A timer already exists for this user")
                    .setEphemeral(true)
                    .queue();

                return;
            }
        }

        Timer timer = new Timer(target.getIdLong(), intr.getUser().getIdLong(), ch, txch);
        Bot.getTimerManager().addTimer(timer);

        intr.reply("A timer was started for " + target.getAsMention())
            .setEphemeral(true)
            .queue();
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("starttimer", "Start a timer for being too late")
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS))
            .addOption(OptionType.USER, "target", "the user to punish for being late", true)
            .addOption(OptionType.CHANNEL, "channel", "the channel to start the timer for", true);
    }
}
