package org.xijinping.bot.event;

import java.util.List;

import org.xijinping.bot.Bot;
import org.xijinping.bot.config.BotConfig.FilterPhrase;
import org.xijinping.bot.savedata.SaveFile;
import org.xijinping.bot.util.EmbedHelper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceiveEventListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // handle filters
        List<FilterPhrase> filters = Bot.getConfig().filter;

        for(FilterPhrase ph : filters) {
            if(!event.getMessage().getContentRaw().toLowerCase().contains(ph.phrase)) {
                continue;
            }

            // grant or remove social credits
            long currentSocialCredits = SaveFile.retrieve().addSocialCredits(event.getAuthor().getIdLong(), ph.amount);

            EmbedBuilder builder = EmbedHelper.createSocialCreditsEmbed(
                ph.amount, event.getAuthor(), currentSocialCredits, String.format("%s mentioned!!!", ph.phrase));

            if(Bot.getConfig().filterChannel.active) {
                TextChannel ch = event.getJDA().getTextChannelById(Bot.getConfig().filterChannel.channel);
                if(ch == null) {
                    return;
                }

                ch.sendMessageEmbeds(builder.build()).queue();
            } else {
                event.getMessage().replyEmbeds(builder.build()).queue();
            }
        }
    }
}
