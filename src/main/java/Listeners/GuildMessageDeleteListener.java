package Listeners;

import DB.MySQLAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildMessageDeleteListener extends ListenerAdapter {

    public void onGuildMessageDelete (@Nonnull GuildMessageDeleteEvent event) {

        MySQLAdapter.deleteReactionRoleMessage(event.getMessageId());

    }
}
