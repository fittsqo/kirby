package Listeners;

import Commands.StarterCommands;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class ReadyListener extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder jda = JDABuilder.createDefault(args[0]);

        jda.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        jda.setBulkDeleteSplittingEnabled(false);
        jda.setActivity(Activity.listening("daddy hasan"));
        jda.addEventListeners(new MemberJoinListener(), new ReactionListener(), new StarterCommands(), new GuildJoinListener());
        jda.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS);

        jda.build();
    }
}
