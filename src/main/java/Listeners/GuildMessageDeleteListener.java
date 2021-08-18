package Listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildMessageDeleteListener extends ListenerAdapter {

    public void onGuildMessageDeleteEvent (@Nonnull GuildMessageDeleteEvent event) {

        // see if it's a reaction role message, delete row if it is

    }
}
