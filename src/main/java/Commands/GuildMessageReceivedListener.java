package Commands;

import DB.MySQLAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.JDAImpl;

import java.util.Objects;

public class GuildMessageReceivedListener extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        JDA jda = event.getJDA();

        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String rawMessage = message.getContentRaw();
        String[] contents = rawMessage.split(" ");
        switch (contents[0]) {
            case "!ping":
                MessageChannel channel = event.getChannel();
                channel.sendMessage("Pong!").queue();
                break;
            case "!simwelcome":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    ((JDAImpl) jda).handleEvent(new GuildMemberJoinEvent(jda, jda.getResponseTotal(), Objects.requireNonNull(event.getMember())));
                    event.getChannel().sendMessage("simulated welcome message. if the message did not show up, try " +
                            "`!setwelcomechannel` and then run the command again.").queue();
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
            case "!setreactionroles":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    int reactionCount = 0;
                    Guild eventGuild = event.getGuild();
                    String reactionRolesMessageId = contents[1];

                    if ((contents.length % 2 == 0) && contents.length > 2) { // validate command
                        if (eventGuild.getGuildChannelById(reactionRolesMessageId) != null) { // validate message exists

                            for (int i = 2; i < contents.length; i++) {
                                // if emote
                                // else if emoji
                                // else if role
                                // else
                            }

                        }
                    }
                }
                break;
        }
    }
}
