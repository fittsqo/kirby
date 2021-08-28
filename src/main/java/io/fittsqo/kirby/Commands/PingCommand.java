package io.fittsqo.kirby.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.ArrayList;

public class PingCommand extends SlashCommand {

    private final String name;
    private final String description;
    private final String usage;
    private final int args;
    private final ArrayList<Permission> permissions;

    public PingCommand() {
        super();
        this.name = "ping";
        this.description = "a simple ping command, mainly for seeing if the bot responds!";
        this.usage = "/ping";
        this.args = 0;
        this.permissions = new ArrayList<>() {};
    }

    @Override
    public void execute(SlashCommandEvent event) {
        event.reply("pong!").queue();
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
