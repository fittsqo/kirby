package Listeners;

import DB.MySQLAdapter;
import com.vdurmont.emoji.EmojiManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class GuildMessageReactionListener extends ListenerAdapter {

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        String reactionId;
        String roleId;

        if (event.getReactionEmote().isEmoji())
            reactionId = event.getReactionEmote().getEmoji();
        else
            reactionId = event.getReactionEmote().getId();

        roleId = MySQLAdapter.getReactionRole(event.getMessageId(), reactionId);

        event.retrieveMember().queue();
        if (roleId != null)
            event.getGuild().addRoleToMember(event.getMember(), Objects.requireNonNull(event.getGuild().getRoleById(roleId))).queue();
    }

    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {
        String reactionId;
        String roleId;

        if (event.getReactionEmote().isEmoji())
            reactionId = event.getReactionEmote().getEmoji();
        else
            reactionId = event.getReactionEmote().getId();

        roleId = MySQLAdapter.getReactionRole(event.getMessageId(), reactionId);

        // TODO: learn how to retrieve members

        if (roleId != null)
            event.getGuild().removeRoleFromMember(event.getMember(), Objects.requireNonNull(event.getGuild().getRoleById(roleId))).queue();
    }
}
