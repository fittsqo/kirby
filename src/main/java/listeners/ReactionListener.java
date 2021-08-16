package listeners;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter {

    private void onGuildMessageReactionAdd() {
        System.out.println("Test");
    }
}
