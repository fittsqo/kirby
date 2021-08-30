package io.fittsqo.kirby.Commands;

import io.fittsqo.kirby.Database.DBAdapter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.EnumSet;

public class ResetCommand extends SlashCommand {

    private final String name;
    private final String description;
    private final int args;
    private final EnumSet<Permission> permissions;
    private final String usage;
    private final DBAdapter dbAdapter;

    public ResetCommand(DBAdapter dbAdapter) {
        super();
        this.name = "reset";
        this.description = "reset all of your server settings!";
        this.usage = "/reset";
        this.args = 0;
        this.permissions = EnumSet.noneOf(Permission.class);
        permissions.add(Permission.ADMINISTRATOR);
        this.dbAdapter = dbAdapter;
    }

    public void execute(SlashCommandEvent event) {
        if (event.getGuild() != null) {
            dbAdapter.resetServer(event.getGuild().getId());
            event.reply("reset all server settings!").queue();
        }
    }

    public String getName() {
        return name;
    }

    public int getArgs() {
        return args;
    }

    public EnumSet<Permission> getPermissions() {
        return permissions;
    }

    public String getDescription() { // mainly for /help
        return description;
    }

    public String getUsage() { // mainly for /help
        return this.usage;
    }
}
