package io.fittsqo.kirby.Listeners;

import io.fittsqo.kirby.Database.DBAdapter;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TextChannelDeleteListener extends ListenerAdapter {

    private final DBAdapter dbAdapter;

    public TextChannelDeleteListener(DBAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    @Override
    public void onTextChannelDelete(@NotNull TextChannelDeleteEvent event) {
        dbAdapter.fixChannelRows(event.getChannel().getId());
    }

}
