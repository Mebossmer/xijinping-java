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
    }
}
