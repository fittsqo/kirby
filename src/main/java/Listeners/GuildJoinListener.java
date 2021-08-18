package Listeners;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildJoinListener extends ListenerAdapter {

    public void onGuildJoin(@Nonnull GuildJoinEvent event) {

        // TODO: initialize SQL (just make a new row and set the primary key to the guild id)

    }
}
