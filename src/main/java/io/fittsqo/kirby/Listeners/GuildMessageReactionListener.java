package io.fittsqo.kirby.Listeners;

import io.fittsqo.kirby.Database.DBAdapter;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildMessageReactionListener extends ListenerAdapter {

    private final DBAdapter dbAdapter;

    public GuildMessageReactionListener(DBAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        Role role = event.getGuild().getRoleById(getRoleId(event.getReactionEmote(), event.getMessageId()));

        if (role != null)
            event.getGuild().addRoleToMember(event.getUserId(), (role)).queue();

    }

    @Override
    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {
        Role role = event.getGuild().getRoleById(getRoleId(event.getReactionEmote(), event.getMessageId()));

        if (role != null)
            event.getGuild().removeRoleFromMember(event.getUserId(), (role)).queue();
    }

    public String getRoleId(MessageReaction.ReactionEmote emote, String messageId) {
        String reactionId;
        String roleId;

        if (emote.isEmoji())
            reactionId = ("U+" + Integer.toHexString(emote.getEmoji().codePointAt(0)));
        else
            reactionId = emote.getId();
        roleId = dbAdapter.getReactionRole(messageId, reactionId);
        return roleId;
    }

}