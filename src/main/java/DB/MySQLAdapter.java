package DB;

import java.sql.*;
import java.util.ArrayList;

public class MySQLAdapter {

    public static void initializeServer(String guildId) {
        // add new row and set welcome message, welcome image, and welcome image message
    }

    public static void resetServer(String guildId) {
        // delete row and re-initialize server
    }

    public static String[] getWelcomeInfo(String guildId) {
        String[] result = new String[4];

        String url = "jdbc:mysql://localhost:3306/kirbybase";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT welcome_channel, welcome_message, welcome_image, welcome_image_message FROM welcome WHERE guild_id = " + guildId);
            ResultSetMetaData md = rs.getMetaData();
            rs.next();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                if (rs.getString(i) == null)
                    result[i - 1] = null;
                else result[i - 1] = rs.getString(i);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        return result;
    }

    public static void setWelcomeChannel(String guildId, String channelId) {
        String url = "jdbc:mysql://localhost:3306/kirbybase";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE welcome SET welcome_channel = " + channelId + " WHERE guild_id = " + guildId);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public static void setWelcomeMessage(String guildId, String welcomeMessage) {
        String url = "jdbc:mysql://localhost:3306/kirbybase";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE welcome SET welcome_message = \"" + welcomeMessage + "\" WHERE guild_id = " + guildId);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public static void setWelcomeImage(String guildId, int welcomeImageId) {
        String url = "jdbc:mysql://localhost:3306/kirbybase";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE welcome SET welcome_image = \"" + welcomeImageId + "\" WHERE guild_id = " + guildId);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public static void setWelcomeImageMessage(String guildId, String welcomeImageMessage) {
        String url = "jdbc:mysql://localhost:3306/kirbybase";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE welcome SET welcome_image_message = \"" + welcomeImageMessage + "\" WHERE guild_id = " + guildId);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public static void createReactionRoleMessage(ArrayList<String[]> reactionRoles) {
        String url = "jdbc:mysql://localhost:3306/kirbybase?useUnicode=yes&characterEncoding=UTF-8";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            for (String[] a : reactionRoles)
                statement.executeUpdate("INSERT INTO reaction_role (`message_id`, `reaction`, `role_id`) VALUES ('" + a[0] + "', '" + a[1] + "', '" + a[2] + "')");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public static String getReactionRole(String messageId, String reaction) {
        String url = "jdbc:mysql://localhost:3306/kirbybase?useUnicode=yes&characterEncoding=UTF-8";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT role_id FROM reaction_role WHERE message_id = '" + messageId + "' AND reaction = '" + reaction + "'");
            if (rs.next())
                return rs.getString("role_id");
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}
