import command.StarterCommands;
import listeners.JoinListener;
import listeners.ReactionListener;
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
        jda.addEventListeners(new JoinListener(), new ReactionListener(), new StarterCommands());
        jda.enableIntents(GatewayIntent.GUILD_MEMBERS);

        jda.build();
    }
}
