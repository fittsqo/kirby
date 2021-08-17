package Commands;

import DB.MySQLInterfacer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.JDAImpl;

import java.util.Objects;

public class StarterCommands extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();

        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (content.equals("!ping")) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Pong!").queue();
        }
        else if (content.equals("!simwelcome")) {
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                ((JDAImpl) jda).handleEvent(new GuildMemberJoinEvent(event.getJDA(), jda.getResponseTotal(), Objects.requireNonNull(event.getMember())));
                event.getChannel().sendMessage("simulated welcome message. if the message did not show up, try " +
                        "`!setwelcomechannel` and then run the command again.").queue();
            }
        }
        else if (content.equals("!setwelcomechannel")) {
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                MySQLInterfacer.setWelcomeChannel(event.getGuild().getId(), event.getChannel().getId());
                event.getChannel().sendMessage("welcome channel set!").queue();
            }
        }
        else if (content.contains("!setwelcomemessage")) {
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                MySQLInterfacer.setWelcomeMessage(event.getGuild().getId(), content.replace("!setwelcomemessage ", ""));
                event.getChannel().sendMessage("welcome message set!").queue();
            }
        }
    }
}
