package io.fittsqo.kirby;

import io.fittsqo.kirby.Database.DBAdapter;
import io.fittsqo.kirby.Listeners.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) throws LoginException {

        if (args.length < 3) {
            System.out.println("You have to provide the bot token, SQL username, and SQL password!");
            System.exit(1);
        }

        JDABuilder jdaBuilder = JDABuilder.createDefault(args[0]);
        DBAdapter dbAdapter = DBAdapter.create();
        dbAdapter.addDSource(args[1], args[2]);

        jdaBuilder.disableCache(
                CacheFlag.MEMBER_OVERRIDES,
                CacheFlag.VOICE_STATE,
                CacheFlag.ACTIVITY,
                CacheFlag.ONLINE_STATUS
        );
        jdaBuilder.setBulkDeleteSplittingEnabled(false);
        jdaBuilder.setActivity(Activity.watching("the stars"));
        jdaBuilder.addEventListeners(
                new ReadyListener(),
                new GuildMemberJoinListener(dbAdapter),
                new GuildMessageReactionListener(dbAdapter),
                new GuildMessageReceivedListener(dbAdapter),
                new GuildJoinListener(dbAdapter),
                new GuildMessageDeleteListener(dbAdapter),
                new GuildLeaveListener(dbAdapter),
                new TextChannelDeleteListener(dbAdapter),
                new SlashCommandListener(dbAdapter)
        );
        jdaBuilder.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_EMOJIS
        );

        jdaBuilder.build();
    }

}
