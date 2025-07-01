package org.xijinping.bot.event;

import org.xijinping.bot.Bot;
import org.xijinping.bot.time.Timer;

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildVoiceUpdateListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        // handle late records

        AudioChannelUnion union = event.getChannelJoined();
        if(union == null) {
            return;
        }

        VoiceChannel joined = union.asVoiceChannel();
        if(joined == null) {
            return;
        }

        for(Timer t : Bot.getTimerManager().getTimers()) {
            if(t.getTargetVoiceChannel().getIdLong() == joined.getIdLong()) {
                // timer stops and evaluates social credits

                t.stopAndEvaluate();
            }
        }
    }

}
