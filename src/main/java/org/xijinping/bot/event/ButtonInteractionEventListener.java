package org.xijinping.bot.event;

import net.dv8tion.jda.api.entities.User;
import org.xijinping.bot.Bot;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.xijinping.bot.gambling.BlackJackManager;

public class ButtonInteractionEventListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();
        User user = event.getUser();

        switch(componentId) {
            case "btn_timer_stop":
                event.deferReply().queue();

                Bot.getTimerManager().getTimers().forEach(t -> {
                    if(t.getStartedByUserId() != event.getUser().getIdLong()) {
                        return;
                    }

                    if(t.getMessageId() != event.getMessageIdLong()) {
                        return;
                    }

                    t.setActive(false);
                });
                break;
            case "btn_blackjack_hit":
                event.deferEdit().queue();

                BlackJackManager.hitForPlayer(user.getIdLong());
                break;
            case "btn_blackjack_pass":
                event.deferEdit().queue();

                BlackJackManager.passForPlayer(user.getIdLong());
                break;
        }
    }
}
