package org.xijinping.bot.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.xijinping.bot.casino.BlackJackManager;

import java.util.Objects;

public class BlackJackCommand implements Command {
    @Override
    public void execute(SlashCommandInteraction intr) {
        long bet = Objects.requireNonNull(intr.getOption("bet")).getAsLong();
        if(bet <= 0) {
            intr.reply("Invalid bet placed").queue();

            return;
        }

        long channelId = intr.getChannelIdLong();
        long userId = intr.getUser().getIdLong();

        BlackJackManager.startNewRound(channelId, userId, bet);

        intr.reply("You Black Jack round has been started")
               .setEphemeral(true)
               .queue();
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("playblackjack", "Play a round of Black Jack and bet your social credits")
                .addOption(OptionType.INTEGER, "bet", "how many social credits you want to bet on this round", true);
    }
}
