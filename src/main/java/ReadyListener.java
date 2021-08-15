import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class ReadyListener implements EventListener {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(args[0]);

        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setActivity(Activity.listening("for commands"));

        builder.build();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        System.out.println("API is ready.");
    }
}
