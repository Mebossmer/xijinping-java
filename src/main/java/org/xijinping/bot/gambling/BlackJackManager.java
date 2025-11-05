package org.xijinping.bot.gambling;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.xijinping.bot.Bot;
import org.xijinping.bot.util.EmbedHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BlackJackManager {
    private static final List<BlackJackInstance> instances = new LinkedList<>();

    public static void startNewRound(long channelId, long userId, long playerBet) {
        TextChannel channel = Bot.getDiscord().getTextChannelById(channelId);
        if(channel == null) {
            return;
        }

        User user = Bot.getDiscord().getUserById(userId);
        if(user == null) {
            return;
        }

        channel.sendMessageEmbeds(EmbedHelper.createBlackJackEmbed(user, 0, 0).build())
                .addActionRow(
                        Button.primary("btn_blackjack_hit", "Hit"),
                        Button.primary("btn_blackjack_pass", "Pass")
                )
                .queue(msg -> {
                    long messageId = msg.getIdLong();
                    instances.add(new BlackJackInstance(userId, messageId, channelId, playerBet));
                });
    }

    public static Optional<BlackJackInstance> getInstanceForUser(long userId) {
        return instances.stream().filter(inst -> inst.getUserId() == userId).findFirst();
    }

    public static void hitForPlayer(long userId) {
        Optional<BlackJackInstance> instance = getInstanceForUser(userId);
        if(instance.isEmpty()) {
            return;
        }

        instance.get().hit();
    }

    public static void passForPlayer(long userId) {
        Optional<BlackJackInstance> instance = getInstanceForUser(userId);
        if(instance.isEmpty()) {
            return;
        }

        instance.get().pass();
    }

    public static List<BlackJackInstance> getInstances() {
        return instances;
    }
}
