package io.fittsqo.kirby.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.ArrayList;

public abstract class SlashCommand {

    private String name;
    private String description;
    private int args;
    private ArrayList<Permission> permissions;
    private String usage;

    public SlashCommand() {

    }

    public void execute(SlashCommandEvent event) {

    }

    public String getName() {
        return name;
    }

    public int getArgs() {
        return args;
    }

    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    public String getDescription() { // mainly for /help
        return description;
    }

    public String getUsage() { // mainly for /help
        return this.usage;
    }
}
