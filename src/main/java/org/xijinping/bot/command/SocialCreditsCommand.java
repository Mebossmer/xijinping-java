package org.xijinping.bot.command;

import org.xijinping.bot.savedata.SaveFile;
import org.xijinping.bot.util.EmbedHelper;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class SocialCreditsCommand extends Command {

    @Override
    public void execute(SlashCommandInteraction intr) {
        User target = intr.getOption("target").getAsUser();
        if(target == null) {
            return;
        }

        long number = intr.getOption("amount").getAsLong();
        if(number == 0) {
            return;
        }

        String reason = "None";
        var optionReason = intr.getOption("reason");
        if(optionReason != null) {
            reason = optionReason.getAsString();
        }

        // grant the social credits
        long currentSocialCredits = SaveFile.retrieve().addSocialCredits(target.getIdLong(), number);

        // send message
        intr.replyEmbeds(EmbedHelper.createSocialCreditsEmbed(number, target, currentSocialCredits, reason).build()).queue();
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("socialcredits", "grant social credits to a user")
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS))
            .addOption(OptionType.USER, "target", "user to target", true)
            .addOption(OptionType.INTEGER, "amount", "number of social credits", true)
            .addOption(OptionType.STRING, "reason", "reason for you decision", false);
    }
}
