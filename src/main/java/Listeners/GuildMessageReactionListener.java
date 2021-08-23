package Listeners;

import DB.MySQLAdapter;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class GuildMessageReactionListener extends ListenerAdapter {

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        String reactionId;
        String roleId;
        Role role;

        if (event.getReactionEmote().isEmoji())
            reactionId = ("U+" + Integer.toHexString(event.getReactionEmote().getEmoji().codePointAt(0)));
        else
            reactionId = event.getReactionEmote().getId();

        roleId = MySQLAdapter.getReactionRole(event.getMessageId(), reactionId);
        if (roleId != null) {
            role = event.getGuild().getRoleById(roleId);
            if (role != null)
                event.getGuild().addRoleToMember(event.getUserId(), (role)).queue();
        }
    }

    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {
        String reactionId;
        String roleId;
        Role role;

        if (event.getReactionEmote().isEmoji())
            reactionId = ("U+" + Integer.toHexString(event.getReactionEmote().getEmoji().codePointAt(0)));
        else
            reactionId = event.getReactionEmote().getId();

        roleId = MySQLAdapter.getReactionRole(event.getMessageId(), reactionId);
        if (roleId != null) {
            role = event.getGuild().getRoleById(roleId);
            if (role != null)
                event.getGuild().removeRoleFromMember(event.getUserId(), (role)).queue();
        }
    }
}
