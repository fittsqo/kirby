package io.fittsqo.kirby.Listeners;

import io.fittsqo.kirby.Database.DBAdapter;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildLeaveListener extends ListenerAdapter {

    private final DBAdapter dbAdapter;

    public GuildLeaveListener(DBAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        dbAdapter.closeServer(event.getGuild().getId());
    }

}
