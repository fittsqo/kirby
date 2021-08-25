package io.fittsqo.kirby.Listeners;

import io.fittsqo.kirby.Database.DBAdapter;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TextChannelDeleteListener extends ListenerAdapter {

    @Override
    public void onTextChannelDelete(@NotNull TextChannelDeleteEvent event) {
        DBAdapter.deleteReactionRoleChannel(event.getChannel().getId());
    }

}
