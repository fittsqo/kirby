package Commands;

import DB.MySQLAdapter;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
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

import java.nio.file.Files;
import java.nio.file.Path;
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
        switch (contents[0]) {
            case "!ping":
                MessageChannel channel = event.getChannel();
                channel.sendMessage("pong!").queue();
                break;
            case "!simwelcome":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    ((JDAImpl) jda).handleEvent(new GuildMemberJoinEvent(jda, jda.getResponseTotal(), Objects.requireNonNull(event.getMember())));
                    event.getChannel().sendMessage("simulated welcome message. if the message did not show up, try " +
                            "`!setwelcomechannel #channel` and then run the command again.").queue();
                }
                break;
            case "!setwelcomechannel":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    String temp;
                    if (contents.length > 1) {
                        if (Pattern.matches(Message.MentionType.CHANNEL.getPattern().toString(), contents[1])) {
                            temp = contents[1].substring(2, contents[1].length() - 1);
                            if (event.getGuild().getTextChannelById(temp) != null) {
                                MySQLAdapter.setWelcomeChannel(event.getGuild().getId(), temp);
                                event.getChannel().sendMessage("welcome channel set: <#" + temp + ">").queue();
                            }
                        }
                        else event.getChannel().sendMessage("channel does not exist!").queue();
                    }
                    else event.getChannel().sendMessage("welcome channel not specified!").queue();
                }
                break;
            case "!setwelcomemessage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    if (contents.length > 1) {
                        if (rawMessage.length() < 2019) {
                            MySQLAdapter.setWelcomeMessage(event.getGuild().getId(), rawMessage.replace("!setwelcomemessage ", ""));
                            event.getChannel().sendMessage("welcome message set!").queue();
                        } else event.getChannel().sendMessage("welcome message must be less than 2000 characters!").queue();
                    } else event.getChannel().sendMessage("invalid welcome message!").queue();
                }
                break;
            case "!setwelcomeimage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    if (Files.exists(Path.of("src/main/resources/images/welcome_blank_" + contents[1] + ".jpg"))) {
                        MySQLAdapter.setWelcomeImage(event.getGuild().getId(), Integer.parseInt(contents[1]));
                        event.getChannel().sendMessage("welcome image id set!").queue();
                    }
                    else event.getChannel().sendMessage("invalid image id!").queue();
                }
                break;
            case "!setwelcomeimagemessage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    if (contents.length > 1) {
                        if (rawMessage.length() < 64) {
                            MySQLAdapter.setWelcomeImageMessage(event.getGuild().getId(), rawMessage.replace("!setwelcomeimagemessage ", ""));
                            event.getChannel().sendMessage("welcome image message set!").queue();
                        } else event.getChannel().sendMessage("welcome image message must be less than 40 characters!").queue();
                    } else event.getChannel().sendMessage("invalid welcome image message!").queue();
                }
                break;
            case "!createrolemessage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    MessageBuilder mb = new MessageBuilder();
                    String temp;
                    Guild eventGuild = event.getGuild();
                    if (Pattern.matches(Message.MentionType.CHANNEL.getPattern().toString(), contents[1])) {
                        // if text channel arg is formatted as a text channel
                        temp = contents[1].substring(2, contents[1].length() - 1);
                        if (eventGuild.getTextChannelById(temp) != null) {
                            // if text channel exists in the server
                            mb.append("found text channel: <#").append(temp).append(">\n");
                            ArrayList<String> emotes = new ArrayList<>();
                            ArrayList<String> roles = new ArrayList<>();

                            boolean looping = true;
                            int i = 2;
                            while (looping) {
                                if (EmojiManager.isEmoji(contents[i])) {
                                    // if this is an emoji
                                    emotes.add(contents[i]);
                                    mb.append("found emoji: ").append(contents[i]).append("\n");
                                } else if (Pattern.matches(Message.MentionType.EMOTE.getPattern().toString(), contents[i])) {
                                    // if this is following custom emote pattern
                                    temp = contents[i].replaceAll("^.*(?=:)", "");
                                    temp = temp.substring(1, temp.length() - 1);
                                    if (eventGuild.getEmoteById(temp) != null) {
                                        // if this is a custom emote in the server
                                        mb.append("found custom emote: ").append(Objects.requireNonNull(eventGuild.getEmoteById(temp)).getAsMention()).append("\n");
                                        emotes.add(temp);
                                    } else {
                                        mb.append("emote not found!\n");
                                    }
                                } else if (Pattern.matches(Message.MentionType.ROLE.getPattern().toString(), contents[i])) {
                                    // if this is following role mention pattern
                                    temp = contents[i].substring(3, contents[i].length() - 1);
                                    if (eventGuild.getRoleById(temp) != null) {
                                        // if this is a role in the server
                                        mb.append("found role: <@&").append(temp).append(">\n");
                                        roles.add(contents[i]);
                                    } else {
                                        mb.append("mentioned role not found!\n");
                                    }
                                } else {
                                    looping = false;
                                }
                                i++;
                                if (i >= contents.length) {
                                    looping = false;
                                }
                            }
                            if (emotes.size() == roles.size()) {
                                // if same number of emotes and roles in message (1 emote maps to 1 reaction)
                                Pattern p = Pattern.compile("\"([^\"]*)\"");
                                Matcher m = p.matcher(rawMessage);
                                if (m.find()) {
                                    temp = m.group().replaceAll("\"", "");
                                    mb.append("found message: ").append(temp).append("\n");
                                } else {
                                    mb.append("no message found!\n");
                                }
                            } else {
                                // more emotes than reactions, or vice versa
                                mb.append("different number of emotes and reactions!\n");
                            }
                        } else {
                            // not valid channel
                            mb.append("text channel not found!\n");
                        }
                    } else {
                        mb.append("text channel missing!\n");
                    }
                    event.getChannel().sendMessage(mb.build()).queue();
                    break;
                }
        }
    }
}