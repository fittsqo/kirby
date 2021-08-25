package io.fittsqo.kirby.Listeners;

import io.fittsqo.kirby.Database.DBAdapter;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        DBAdapter.initializeServer(event.getGuild().getId());
    }

}
