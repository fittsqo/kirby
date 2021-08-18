import Commands.StarterCommands;
import Listeners.GuildJoinListener;
import Listeners.MemberJoinListener;
import Listeners.ReactionListener;
import Listeners.ReadyListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) throws LoginException {

        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }

        JDABuilder jda = JDABuilder.createDefault(args[0]);

        jda.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        jda.setBulkDeleteSplittingEnabled(false);
        jda.setActivity(Activity.listening("daddy hasan"));
        jda.addEventListeners(
                new ReadyListener(),
                new MemberJoinListener(),
                new ReactionListener(),
                new StarterCommands(),
                new GuildJoinListener());
        jda.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS);

        jda.build();
    }

}
