package Utilities;

public class Config {

    public static String[] getDefaultWelcome() {
        String[] defaultWelcome = new String[3];
        defaultWelcome[0] = "hi %user_mention% <3 welcome to %guild_name% :)";
        defaultWelcome[1] = "0";
        defaultWelcome[2] = "%user_tag% joined the server";
        return defaultWelcome;
    }
}
