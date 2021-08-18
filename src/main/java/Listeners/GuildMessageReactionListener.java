package Listeners;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildMessageReactionListener extends ListenerAdapter {

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        System.out.println("=====================");
        System.out.println("Guild: " + event.getGuild());
        System.out.println("User: " + event.getUser());
        System.out.println("Channel: " + event.getChannel());
        System.out.println("Message: " + event.getReaction().getMessageId());
        System.out.println("=====================");

        System.out.println((event.getReactionEmote().isEmoji()));
    }
}
