package io.fittsqo.kirby.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class HelpCommand extends SlashCommand {

    private final String name;
    private final String description;
    private final String usage;
    private final int args;
    private final ArrayList<Permission> permissions;

    private final HashMap<String, SlashCommand> commands;

    public HelpCommand(HashMap<String, SlashCommand> commands) {
        super();
        this.name = "help";
        this.description = "your own personal help command!";
        this.usage = "/help";
        this.args = 0;
        this.permissions = new ArrayList<>() {};
        this.commands = commands;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        event.reply("not implemented yet!").queue();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }

    public int getArgs() {
        return args;
    }

    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    public String getUsage() {
        return usage;
    }
}
