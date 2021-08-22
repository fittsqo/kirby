package Commands;

import DB.MySQLAdapter;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.EmoteManager;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.EmoteImpl;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.TextChannelImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildMessageReceivedListener extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        JDA jda = event.getJDA();

        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String rawMessage = message.getContentRaw();
        String[] contents = rawMessage.split("[ \n]");
        System.out.println(Arrays.toString(contents));
        switch (contents[0]) {
            case "!ping":
                MessageChannel channel = event.getChannel();
                channel.sendMessage("pong!").queue();
                break;
            case "!simwelcome":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    ((JDAImpl) jda).handleEvent(new GuildMemberJoinEvent(jda, jda.getResponseTotal(), Objects.requireNonNull(event.getMember())));
                    event.getChannel().sendMessage("simulated welcome message. if the message did not show up, try " +
                            "!setwelcomechannel and then run the command again.").queue();
                }
                break;
            case "!setwelcomechannel": // TODO implement setwelcomechannel with Pattern.matches(Message.MentionType.CHANNEL...)
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    MySQLAdapter.setWelcomeChannel(event.getGuild().getId(), event.getChannel().getId());
                    event.getChannel().sendMessage("welcome channel set!").queue();
                }
                break;
            case "!setwelcomemessage": // TODO implement setwelcomeimagemessage and setwelcomeimage
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    MySQLAdapter.setWelcomeMessage(event.getGuild().getId(), rawMessage.replace("!setwelcomemessage ", ""));
                    event.getChannel().sendMessage("welcome message set!").queue();
                }
                break;
            case "!createrolemessage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    String msg;
                    String temp;
                    Guild eventGuild = event.getGuild();
                    if (contents[1].matches("\\d+"))
                        if (eventGuild.getTextChannelById(contents[1]) != null) {
                            // if this is a valid channel
                            event.getChannel().sendMessage("found text channel!").queue();

                            ArrayList<String> emotes = new ArrayList<>();
                            ArrayList<String> roles = new ArrayList<>();

                            boolean looping = true;
                            int i = 2;
                            while (looping) {
                                if (EmojiManager.isEmoji(contents[i])) {
                                    // if this is an emoji
                                    emotes.add(contents[i]);
                                    event.getChannel().sendMessage("found emoji in message!").queue();
                                } else if (Pattern.matches(Message.MentionType.EMOTE.getPattern().toString(), contents[i])) {
                                    // if this is following custom emote pattern
                                    temp = contents[i].replaceAll("^.*(?=:)", "");
                                    temp = temp.substring(1, temp.length()-1);
                                    if (eventGuild.getEmoteById(temp) != null) {
                                        // if this is a custom emote in the server
                                        event.getChannel().sendMessage("found custom emote in message!").queue();
                                        emotes.add(temp);
                                    }
                                    else
                                        event.getChannel().sendMessage("custom emote is not in this server!").queue();
                                } else if (Pattern.matches(Message.MentionType.ROLE.getPattern().toString(), contents[i])) {
                                    // if this is following role mention pattern
                                    temp = contents[i].replaceAll("^.*(?=&)", "");
                                    temp = temp.substring(1, temp.length()-1);
                                    if (eventGuild.getRoleById(temp) != null) {
                                        // if this is a role in the server
                                        event.getChannel().sendMessage("found role in message!").queue();
                                        roles.add(contents[i]);
                                    } else
                                        event.getChannel().sendMessage("mentioned role does not exist!").queue();
                                } else if (contents[i].charAt(0) == '\"') {
                                    // beginning of quote wrapped message
                                    looping = false;
                                } else {
                                    event.getChannel().sendMessage("you did something wrong lul (or brian did xd)").queue();
                                    looping = false;
                                }
                                i++;
                            }

                            if (emotes.size() != roles.size()) {

                                Pattern p = Pattern.compile("\"([^\"]*)\"");
                                Matcher m = p.matcher(rawMessage);
                                if (m.find())
                                    msg = m.group().replaceAll("\"", "");
                                else
                                    msg = "default message";
                                System.out.println(msg);
                            }
                        } else {
                            // not valid channel
                            event.getChannel().sendMessage("unknown text channel!").queue();
                        }
                    else
                        event.getChannel().sendMessage("nonexistent text channel!").queue();
                }
                break;
        }
    }
}
