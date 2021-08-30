package io.fittsqo.kirby.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.EnumSet;

public class PingCommand extends SlashCommand {

    private final String name;
    private final String description;
    private final String usage;
    private final int args;
    private final EnumSet<Permission> permissions;

    public PingCommand() {
        this.name = "ping";
        this.description = "a simple ping command, mainly for testing if the bot responds!";
        this.usage = "/ping";
        this.args = 0;
        this.permissions = EnumSet.noneOf(Permission.class);
    }

    @Override
    public void execute(SlashCommandEvent event) {
        event.reply("pong!").setEphemeral(true).queue();
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

    public EnumSet<Permission> getPermissions() {
        return permissions;
    }

    public String getUsage() {
        return usage;
    }
}
