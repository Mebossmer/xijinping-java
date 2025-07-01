package org.xijinping.bot.event;

import org.xijinping.bot.Bot;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonInteractionEventListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // handle the stop button on timers

        if(!event.getComponentId().equals("btn_timer_stop")) {
            return;
        }

        Bot.getTimerManager().getTimers().forEach(t -> {
            if(t.getStartedByUserId() != event.getUser().getIdLong()) {
                return;
            }
            
            if(t.getMessageId() != event.getMessageIdLong()) {
                return;
            }
            
            t.setActive(false);
        });

        /*
        if(!event.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
            EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xFF0000)
                .setTitle("You don't have the permission to do that!")
                .setTimestamp(Instant.now());

            event.replyEmbeds(builder.build()).setEphemeral(true);

            return;
        }
        */

        /*
        Timer.stopTimer(event.getMessage().getId());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(0xFF0000);
        builder.setTitle("⏲️ YOU ARE LATE ⏲️");
        builder.setDescription("Timer stopped");
        builder.setTimestamp(Instant.now());

        event.getMessage().editMessageEmbeds(builder.build()).queue();
        event.getMessage().editMessageComponents().queue();

        EmbedBuilder builder2 = new EmbedBuilder();
        builder2.setColor(0xFF0000);
        builder2.setTitle("Timer stopped successfully");
        builder2.setTimestamp(Instant.now());

        event.replyEmbeds(builder2.build()).setEphemeral(true).queue();
        */
    }
}
