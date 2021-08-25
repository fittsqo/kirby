package io.fittsqo.kirby.Listeners;

import io.fittsqo.kirby.Database.DBAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildMessageDeleteListener extends ListenerAdapter {

    private DBAdapter dbAdapter;

    public GuildMessageDeleteListener(DBAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    @Override
    public void onGuildMessageDelete (@Nonnull GuildMessageDeleteEvent event) {
        dbAdapter.deleteReactionRoleMessage(event.getMessageId());
    }

}
