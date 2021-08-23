package DB;

import java.sql.*;
import java.util.ArrayList;

public class DBAdapter {

    public static void initializeServer(String guildId) {
        String url = "jdbc:mysql://localhost:3306/kirbybase?useUnicode=yes&characterEncoding=UTF-8";
        String username = "java";
        String password = "password";

        String[] joinValues = new String[3];
        joinValues[0] = "hi %user_id% <3 welcome to %guild_name% :)";
        joinValues[1] = "0";
        joinValues[2] = "%user_tag% joined the server";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO welcome (`guild_id`, `welcome_message`, `welcome_image`, `welcome_image_message`) VALUES ('" + guildId + "', '" + joinValues[0] + "', '" + joinValues[1] + "', '" + joinValues[2] + "')");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

    public static void closeServer(String guildId) {
        String url = "jdbc:mysql://localhost:3306/kirbybase";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM reaction_role WHERE guild_id = " + guildId);
            statement.executeUpdate("DELETE FROM welcome WHERE guild_id = " + guildId);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public static void resetServer(String guildId) {
        closeServer(guildId);
        initializeServer(guildId);
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

    public static void createReactionRoleMessage(String guildId, String channelId, ArrayList<String[]> reactionRoles) {
        String url = "jdbc:mysql://localhost:3306/kirbybase?useUnicode=yes&characterEncoding=UTF-8";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            for (String[] a : reactionRoles)
                statement.executeUpdate("INSERT INTO reaction_role (`message_id`, `reaction`, `role_id`, `guild_id`, `channel_id`) VALUES ('" + a[0] + "', '" + a[1] + "', '" + a[2] + "', '" + guildId + "', '" + channelId + "')");
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

    public static void deleteReactionRoleMessage(String messageId) {
        String url = "jdbc:mysql://localhost:3306/kirbybase";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM reaction_role WHERE message_id = " + messageId);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public static void deleteReactionRoleChannel(String channelId) {
        String url = "jdbc:mysql://localhost:3306/kirbybase";
        String username = "java";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM reaction_role WHERE channel_id = " + channelId);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}
