package org.xijinping.bot.casino;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.xijinping.bot.Bot;
import org.xijinping.bot.savedata.SaveFile;
import org.xijinping.bot.util.EmbedHelper;

import java.util.*;

public class BlackJackInstance {
    // available cards on the stack
    private final List<Card.Type> cardDeck = new ArrayList<>();

    // cards the player has already drawn
    private final List<Card.Type> playerCards = new ArrayList<>();

    // cards the dealer has drawn
    private final List<Card.Type> dealerCards = new ArrayList<>();

    private final long textChannelId;
    private final long messageId;
    private final long userId;
    private final long playerBet;

    public BlackJackInstance(long userId, long messageId, long textChannelId, long playerBet) {
        this.textChannelId = textChannelId;
        this.messageId = messageId;
        this.userId = userId;
        this.playerBet = playerBet;

        for(Card.Type type : Card.Type.values()) {
            for(int i = 0; i < 4; i++) {
                cardDeck.add(type);
            }
        }

        Collections.shuffle(cardDeck);
    }

    public void hit() {
        // handle player's turn
        playerCards.add(cardDeck.removeLast());

        if(getCardsValue(playerCards) > 21) {
            lose();
        }

        updateMessage();
    }

    public void pass() {
        // handle dealer's turn
        while(getCardsValue(dealerCards) < 17) {
            dealerCards.add(cardDeck.removeLast());
        }

        int dealerValue = getCardsValue(dealerCards);
        int playerValue = getCardsValue(playerCards);

        if(dealerValue > 21) {
            win();

            return;
        }

        if(playerValue > dealerValue) {
            win();

            return;
        }

        lose();
    }

    public void lose() {
        TextChannel textChannel = Bot.getDiscord().getTextChannelById(textChannelId);
        if(textChannel == null) {
            return;
        }

        User user = Bot.getDiscord().getUserById(userId);
        if(user == null) {
            return;
        }

        int playerValue = getCardsValue(playerCards);
        int dealerValue = getCardsValue(dealerCards);

        textChannel.retrieveMessageById(messageId)
                .queue(msg -> msg.editMessageEmbeds(EmbedHelper.createBlackJackLostEmbed(user, playerValue, dealerValue).build()).queue());

        // grant the social credits
        long currentSocialCredits = SaveFile.retrieve().addSocialCredits(userId, -playerBet);

        // send message
        textChannel.sendMessageEmbeds(EmbedHelper.createSocialCreditsEmbed(-playerBet, user, currentSocialCredits, "Lost a round of Black Jack").build()).queue();

        BlackJackManager.getInstances().remove(this);
    }

    public void win() {
        TextChannel textChannel = Bot.getDiscord().getTextChannelById(textChannelId);
        if(textChannel == null) {
            return;
        }

        User user = Bot.getDiscord().getUserById(userId);
        if(user == null) {
            return;
        }

        int playerValue = getCardsValue(playerCards);
        int dealerValue = getCardsValue(dealerCards);

        textChannel.retrieveMessageById(messageId)
                .queue(msg -> msg.editMessageEmbeds(EmbedHelper.createBlackJackWonEmbed(user, playerValue, dealerValue).build()).queue());

        long granted = playerBet * 2L;

        // grant the social credits
        long currentSocialCredits = SaveFile.retrieve().addSocialCredits(userId, granted);

        // send message
        textChannel.sendMessageEmbeds(EmbedHelper.createSocialCreditsEmbed(granted, user, currentSocialCredits, "Won a round of Black Jack").build()).queue();

        BlackJackManager.getInstances().remove(this);
    }

    public static int getCardsValue(List<Card.Type> cards) {
        int value = 0;

        for(Card.Type type : cards) {
            if(type == Card.Type.ACE) {
                // handle aces

                if(value + 11 <= 21) {
                    value += 11;
                } else {
                    value += 1;
                }
            } else {
                value += type.value;
            }
        }

        return value;
    }

    public long getUserId() {
        return userId;
    }

    public void updateMessage() {
        TextChannel textChannel = Bot.getDiscord().getTextChannelById(textChannelId);
        if(textChannel == null) {
            return;
        }

        User user = Bot.getDiscord().getUserById(userId);
        if(user == null) {
            return;
        }

        int playerValue = getCardsValue(playerCards);
        int dealerValue = getCardsValue(dealerCards);

        textChannel.retrieveMessageById(messageId)
                .queue(msg -> msg.editMessageEmbeds(EmbedHelper.createBlackJackWonEmbed(user, playerValue, dealerValue).build()).queue());
    }

    /*
    public boolean didPlayerWin() {
        return (getCardsValue(playerCards) > getCardsValue(dealerCards));
    }

    public void doDealersTurn() {
        while(getCardsValue(dealerCards) < 17) {
            drawOneCard(dealerCards);
        }
    }

    private boolean drawOneCard(List<Card.Type> cards) {
        cards.add(cardDeck.removeLast());

        int value = getCardsValue(playerCards);

        return value > 21;
    }

    public void drawOneCardForPlayer() {
        if(drawOneCard(playerCards)) {
            lose();
        }
    }

    public static int getCardsValue(List<Card.Type> cards) {
        int value = 0;

        for(Card.Type type : cards) {
            value += type.value;
        }

        return value;
    }
    */
}
