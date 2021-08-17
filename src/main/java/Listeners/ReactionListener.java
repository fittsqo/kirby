package Listeners;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter {

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        System.out.println("=====================");
        System.out.println("Member: " + event.getMember());
        System.out.println("Channel: " + event.getChannel().getId());
        System.out.println("Message: " + event.getReaction().getMessageId());
        System.out.println("Emote: " + event.getReaction().getReactionEmote());
        System.out.println("=====================");
    }
}
