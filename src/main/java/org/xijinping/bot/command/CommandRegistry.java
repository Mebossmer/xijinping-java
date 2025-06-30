package org.xijinping.bot.command;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandRegistry extends ListenerAdapter {
    private List<Command> commands = new ArrayList<>();

    public void register() {
        commands.add(new LashCommand());
        commands.add(new StartTimerCommand());
        commands.add(new SocialCreditsCommand());
        commands.add(new LeaderBoardCommand());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        event.deferReply();

        String name = event.getName();
        for(Command c : commands) {
            if(c.getData().getName().equals(name)) {
                c.execute(event.getInteraction());
                break;
            }
        }

        System.out.println(event.getUser().getName() + " executed /" + name);
    }

    public void deploy(JDA jda) {
        List<SlashCommandData> data = new ArrayList<>();
        for(Command c : commands) {
            data.add(c.getData());
        }

        jda.updateCommands().addCommands(data).queue();

        System.out.println("Deployed " + data.size() + " application commands");
    }
}
