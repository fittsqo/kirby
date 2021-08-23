package Listeners;

import DB.DBAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildMessageDeleteListener extends ListenerAdapter {

    public void onGuildMessageDelete (@Nonnull GuildMessageDeleteEvent event) {

        DBAdapter.deleteReactionRoleMessage(event.getMessageId());

    }
}
