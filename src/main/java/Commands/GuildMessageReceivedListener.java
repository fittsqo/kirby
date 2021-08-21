package Commands;

import DB.MySQLAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.TextChannelImpl;

import java.util.Arrays;
import java.util.Objects;

public class GuildMessageReceivedListener extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        JDA jda = event.getJDA();

        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String rawMessage = message.getContentRaw();
        String[] contents = rawMessage.split("[ \n]");
        System.out.println(rawMessage);
        System.out.println(Arrays.toString(contents));
        switch (contents[0]) {
            case "!ping":
                MessageChannel channel = event.getChannel();
                channel.sendMessage("Pong!").queue();
                break;
            case "!simwelcome":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    ((JDAImpl) jda).handleEvent(new GuildMemberJoinEvent(jda, jda.getResponseTotal(), Objects.requireNonNull(event.getMember())));
                    event.getChannel().sendMessage("simulated welcome message. if the message did not show up, try " +
                            "!setwelcomechannel and then run the command again.").queue();
                }
                break;
            case "!setwelcomechannel":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    MySQLAdapter.setWelcomeChannel(event.getGuild().getId(), event.getChannel().getId());
                    event.getChannel().sendMessage("welcome channel set!").queue();
                }
                break;
            case "!setwelcomemessage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    MySQLAdapter.setWelcomeMessage(event.getGuild().getId(), rawMessage.replace("!setwelcomemessage ", ""));
                    event.getChannel().sendMessage("welcome message set!").queue();
                }
                break;
            case "!createrolemessage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    int emoteCount = 0;
                    int roleCount = 0;
                    Guild eventGuild = event.getGuild();
                    if (eventGuild.getChannels().contains(new TextChannelImpl(Long.parseLong(contents[1]), (GuildImpl)eventGuild))) {
                        // if this is a valid channel
                    } else {
                        // not valid channel
                        event.getChannel().sendMessage("invalid text channel!").queue();
                    }
                    for (String s : contents) {

                    }
                }
                break;
        }
    }
}
