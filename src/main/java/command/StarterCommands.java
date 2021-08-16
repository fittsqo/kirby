package command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.JDAImpl;

import java.util.Objects;

public class StarterCommands extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event)
    {
        JDA jda = event.getJDA();

        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (content.equals("!ping"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Pong!").queue();
        } else if (content.equals("!simwelcome")) {
            ((JDAImpl)jda).handleEvent(new GuildMemberJoinEvent(event.getJDA(), jda.getResponseTotal(), Objects.requireNonNull(event.getMember())));
        }
    }
}
