package io.fittsqo.kirby.Listeners;

import io.fittsqo.kirby.Commands.HelpCommand;
import io.fittsqo.kirby.Commands.PingCommand;
import io.fittsqo.kirby.Commands.SlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class SlashCommandListener extends ListenerAdapter {

    private final HashMap<String, SlashCommand> commands = new HashMap<>();

    public SlashCommandListener() {
        addCommands(
                new PingCommand(),
                new HelpCommand(commands)
                );
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        SlashCommand command = commands.get(event.getName());
        Member invoker = event.getMember();
        if (invoker != null)
            if (invoker.hasPermission(command.getPermissions()))
                command.execute(event);
            else
                event.reply("You do not have permission to run this command!").queue();
    }

    public void addCommands(SlashCommand... command) {
        for (SlashCommand i : command)
            commands.put(i.getName(), i);
    }

}
