package io.fittsqo.kirby.Database;

import io.fittsqo.kirby.Utilities.Config;

import java.sql.*;
import java.util.ArrayList;

public class DBAdapter {

    private final DSource dSource;

    public DBAdapter(String username, String password) {
        dSource = new DSource(username, password);
    }

    public void initializeServer(String guildId) {
        String[] joinValues = Config.getDefaultWelcome();
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO welcome (guild_id, welcome_message, welcome_image, welcome_image_message) VALUES (?, ?, ?, ?)");
            ps.setString(1, guildId);
            ps.setString(2, joinValues[0]);
            ps.setString(3, joinValues[1]);
            ps.setString(4, joinValues[2]);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void closeServer(String guildId) {
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM reaction_role WHERE guild_id = ?");
            ps.setString(1, guildId);
            ps.executeUpdate();
            ps = conn.prepareStatement("DELETE FROM welcome WHERE guild_id = ?");
            ps.setString(1, guildId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void resetServer(String guildId) {
        closeServer(guildId);
        initializeServer(guildId);
    }


    public String[] getWelcomeInfo(String guildId) {
        String[] result = new String[4];
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT welcome_channel, welcome_message, welcome_image, welcome_image_message FROM welcome WHERE guild_id = ?");
            ps.setString(1, guildId);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            if (rs.next()) {
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    if (rs.getString(i) == null)
                        result[i - 1] = null;
                    else result[i - 1] = rs.getString(i);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        return result;
    }

    public void setWelcomeChannel(String guildId, String channelId) {
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE welcome SET welcome_channel = ? WHERE guild_id = ?");
            ps.setString(1, channelId);
            ps.setString(2, guildId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void setWelcomeMessage(String guildId, String welcomeMessage) {
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE welcome SET welcome_message = ? WHERE guild_id = ?");
            ps.setString(1, welcomeMessage);
            ps.setString(2, guildId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void setWelcomeImage(String guildId, int welcomeImageId) {
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE welcome SET welcome_image = ? WHERE guild_id = ?");
            ps.setInt(1, welcomeImageId);
            ps.setString(2, guildId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void setWelcomeImageMessage(String guildId, String welcomeImageMessage) {
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE welcome SET welcome_image_message = ? WHERE guild_id = ?");
            ps.setString(1, welcomeImageMessage);
            ps.setString(2, guildId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void createReactionRoleMessage(String guildId, String channelId, ArrayList<String[]> reactionRoles) {
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps;
            for (String[] a : reactionRoles) {
                ps = conn.prepareStatement("INSERT INTO reaction_role (message_id, reaction, role_id, guild_id, channel_id) VALUES (?, ?, ?, ?, ?)");
                ps.setString(1, a[0]);
                ps.setString(2, a[1]);
                ps.setString(3, a[2]);
                ps.setString(4, guildId);
                ps.setString(5, channelId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public String getReactionRole(String messageId, String reaction) {
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT role_id FROM reaction_role WHERE message_id = ? AND reaction = ?");
            ps.setString(1, messageId);
            ps.setString(2, reaction);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getString("role_id");
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void deleteReactionRoleMessage(String messageId) {
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM reaction_role WHERE message_id = ?");
            ps.setString(1, messageId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void deleteReactionRoleChannel(String channelId) {
        try (Connection conn = dSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM reaction_role WHERE channel_id = ?");
            ps.setString(1, channelId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}
