package Commands;

import DB.DBAdapter;
import com.vdurmont.emoji.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.JDAImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildMessageReceivedListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        JDA jda = event.getJDA();

        if (event.getAuthor().isBot()) return;
        Message commandMessage = event.getMessage();
        String rawMessage = commandMessage.getContentRaw();
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
                            "!setwelcomechannel #channel, and then run the command again.").queue();
                }
                break;

            case "!setwelcomechannel":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    String temp;
                    if (contents.length > 1) {
                        if (Pattern.matches(Message.MentionType.CHANNEL.getPattern().toString(), contents[1])) {
                            temp = contents[1].substring(2, contents[1].length() - 1);
                            if (event.getGuild().getTextChannelById(temp) != null) {
                                DBAdapter.setWelcomeChannel(event.getGuild().getId(), temp);
                                event.getChannel().sendMessage("welcome channel set: <#" + temp + ">").queue();
                            }
                        }
                        else event.getChannel().sendMessage("channel does not exist!").queue();
                    }
                    else event.getChannel().sendMessage("welcome channel not specified! (use #channel after the command)").queue();
                }
                break;

            case "!setwelcomemessage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    if (contents.length > 1) {
                        if (rawMessage.length() < 2019) {
                            DBAdapter.setWelcomeMessage(event.getGuild().getId(), rawMessage.replace("!setwelcomemessage ", ""));
                            event.getChannel().sendMessage("welcome message set!").queue();
                        } else event.getChannel().sendMessage("welcome message must be less than 2000 characters!").queue();
                    } else event.getChannel().sendMessage("u need to add a welcome message!").queue();
                }
                break;

            case "!setwelcomeimage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    if (contents.length > 1) {
                        if (Files.exists(Path.of("src/main/resources/images/welcome_blank_" + contents[1] + ".jpg"))) {
                            DBAdapter.setWelcomeImage(event.getGuild().getId(), Integer.parseInt(contents[1]));
                            event.getChannel().sendMessage("welcome image id set!").queue();
                        } else
                            event.getChannel().sendMessage("invalid image id! (only 1 and 0 are currently supported)").queue();
                    } else event.getChannel().sendMessage("specify which image to set! (0 or 1)").queue();

                }
                break;

            case "!setwelcomeimagemessage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    if (contents.length > 1) {
                        if (rawMessage.length() < 64) {
                            DBAdapter.setWelcomeImageMessage(event.getGuild().getId(), rawMessage.replace("!setwelcomeimagemessage ", ""));
                            event.getChannel().sendMessage("welcome image message set!").queue();
                        } else event.getChannel().sendMessage("welcome image message must be less than 40 characters!").queue();
                    } else event.getChannel().sendMessage("add a welcome image message!").queue();
                }
                break;

            case "!createrolemessage":
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                    MessageBuilder mb = new MessageBuilder();
                    TextChannel textChannel; // where the reaction role message will be sent
                    Guild eventGuild = event.getGuild();
                    String temp;

                    if (contents.length > 1) {
                        if (Pattern.matches(Message.MentionType.CHANNEL.getPattern().toString(), contents[1])) {
                            // if text channel arg is formatted as a text channel
                            temp = contents[1].substring(2, contents[1].length() - 1);
                            if (eventGuild.getTextChannelById(temp) != null) {
                                // if text channel exists in the server
                                textChannel = eventGuild.getTextChannelById(temp);
                                LinkedHashSet<String> emotesLh = new LinkedHashSet<>();
                                LinkedHashSet<String> rolesLh = new LinkedHashSet<>();

                                // being parsing all the arguments in the command
                                boolean looping = true;
                                int i = 2;
                                while (looping) {
                                    if (EmojiManager.isEmoji(contents[i])) {
                                        // if arg is an emoji
                                        emotesLh.add(contents[i]);
                                    } else if (Pattern.matches(Message.MentionType.EMOTE.getPattern().toString(), contents[i])) {
                                        // if arg is in custom emote pattern
                                        temp = contents[i].replaceAll("^.*(?=:)", "");
                                        temp = temp.substring(1, temp.length() - 1);
                                        if (eventGuild.getEmoteById(temp) != null) {
                                            // if arg is a custom emote in the server
                                            emotesLh.add(temp);
                                        } else {
                                            mb.append("emote not found!\n");
                                        }
                                    } else if (Pattern.matches(Message.MentionType.ROLE.getPattern().toString(), contents[i])) {
                                        // if arg is following role mention pattern
                                        temp = contents[i].substring(3, contents[i].length() - 1);
                                        if (eventGuild.getRoleById(temp) != null) {
                                            // if arg is a role in the server
                                            if (!rolesLh.contains(temp)) {
                                                if (eventGuild.getSelfMember().getRoles().get(0).canInteract(Objects.requireNonNull(eventGuild.getRoleById(temp)))) {
                                                    rolesLh.add(temp);
                                                } else mb.append("i dont have perms to give this role!\n");
                                            } else {
                                                mb.append("each role should be unique!\n");
                                            }
                                        } else {
                                            mb.append("i couldnt find that mentioned role!\n");
                                        }
                                    } else {
                                        looping = false;
                                    }
                                    i++;
                                    if (i >= contents.length) {
                                        looping = false;
                                    }
                                }

                                ArrayList<String> emotes = new ArrayList<>(emotesLh);
                                ArrayList<String> roles = new ArrayList<>(rolesLh);

                                if (emotes.size() > 0 && roles.size() > 0) {
                                    if (emotes.size() == roles.size()) {
                                        // if same number of emotes and roles in message (1 emote maps to 1 reaction)

                                        Pattern p = Pattern.compile("\"([^\"]*)\"");
                                        Matcher m = p.matcher(rawMessage);

                                        ArrayList<String[]> reactionRoles = new ArrayList<>();

                                        // get the message text from the command
                                        if (m.find()) {
                                            temp = m.group().replaceAll("\"", "");
                                            assert textChannel != null;
                                            textChannel.sendMessage(temp).queue(t -> {
                                                for (int j = 0; j < roles.size(); j++) {
                                                    if (EmojiManager.isEmoji(emotes.get(j))) {
                                                        t.addReaction(emotes.get(j)).queue();
                                                        emotes.set(j, "U+" + Integer.toHexString(emotes.get(j).codePointAt(0)));
                                                    } else {
                                                        t.addReaction(Objects.requireNonNull(eventGuild.getEmoteById(emotes.get(j)))).queue();
                                                    }
                                                    reactionRoles.add(new String[]{
                                                            t.getId(), emotes.get(j), roles.get(j)
                                                    });
                                                }
                                                DBAdapter.createReactionRoleMessage(eventGuild.getId(), textChannel.getId(), reactionRoles);
                                                mb.append("reaction role message sent!\n");
                                            });
                                        } else {
                                            mb.append("you must specify a message after the roles and reactions in quotes\n");
                                        }

                                    } else {
                                        // more emotes than reactions, or vice versa
                                        mb.append("you need to specify same number of emotes and roles!\n");
                                    }
                                } else {
                                    // no emotes and
                                    mb.append("you need at least 1 valid emote and role!\n");
                                }
                            } else {
                                // not valid channel
                                mb.append("i couldnt find that text channel!\n");
                            }
                        } else {
                            mb.append("you forgot to include the text channel! (use #channel)\n");
                        }
                    }
                    else {
                        mb.append("specify the text #channel after the command!");
                    }
                    if (!mb.isEmpty())
                        event.getChannel().sendMessage(mb.build()).queue();
                }
                break;
            case ("!reset"):
                DBAdapter.resetServer(event.getGuild().getId());
                event.getChannel().sendMessage("reset all server settings (including reaction roles and welcome channel)!").queue();
                break;
        }
    }

}