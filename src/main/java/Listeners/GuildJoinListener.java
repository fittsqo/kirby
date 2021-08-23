package Listeners;

import DB.MySQLAdapter;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class GuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {

        String[] joinValues = new String[3];
        joinValues[0] = "hi %user_id% <3 welcome to %guild_name% :)";
        joinValues[1] = "0";
        joinValues[2] = "%user_tag% joined the server";

        MySQLAdapter.initializeServer(event.getGuild().getId(), joinValues);

    }
}
