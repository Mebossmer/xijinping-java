package org.xijinping.bot.util;

import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.User;

public class EmbedHelper {
    public static EmbedBuilder createSocialCreditsEmbed(
        long amount,
        User user,
        long currentAmount,
        String reason
    ) {
        return new EmbedBuilder()
            .setColor(0xFF0000)
            .setTitle("🚨 SOCIAL CREDITS 🚨")
            .setDescription(String.format("%d social credits for %s", amount, user.getAsMention()))
            .addField(new Field("Current social credits", String.valueOf(currentAmount), true))
            .addField(new Field("Reason", reason, true))
            .setImage(amount >= 0 
                ? "https://c.tenor.com/Ka4PNc1meRsAAAAd/tenor.gif"
                : "https://c.tenor.com/F-D5EhlQXdMAAAAd/tenor.gif")
            .setTimestamp(Instant.now());
    }
}
