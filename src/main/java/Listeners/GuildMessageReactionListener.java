package Listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class GuildMessageReactionListener extends ListenerAdapter {

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
//        System.out.println("Guild: " + event.getGuild());
//        System.out.println("User: " + event.getUser());
//        System.out.println("Channel: " + event.getChannel());
//        System.out.println("Message: " + event.getMessageId());
//        System.out.println("Reaction: " + event.getReactionEmote());
//        System.out.println("=====================");
    }

    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {

        // TODO remove role when reaction removed

    }
}
