package io.fittsqo.kirby.Listeners;

import io.fittsqo.kirby.Database.DBAdapter;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class GuildMemberJoinListener extends ListenerAdapter {

    private final int BG_WIDTH = 1696;
    private final int BG_HEIGHT = 954;
    private final int PFP_DIM = 512; // image will be scaled to this anyways...
    private final int MARGIN = 30;

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        String[] welcomeInfo = DBAdapter.getWelcomeInfo(event.getGuild().getId());
        if (welcomeInfo[0] != null) { // if the welcome message is set
            User user = event.getUser();
            try {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/coolvetica rg.ttf")));

                BufferedImage pfp = new BufferedImage(PFP_DIM, PFP_DIM, TYPE_INT_RGB);
                BufferedImage rawPfp = ImageIO.read(new URL(user.getEffectiveAvatarUrl() + "?size=" + PFP_DIM));

                int welcomeImageId = Integer.parseInt(welcomeInfo[2]);

                String BG_PATH = "src/main/resources/images/welcome_blank_" + welcomeImageId + ".jpg";
                BufferedImage background = ImageIO.read(new File(BG_PATH));
                if (background.getHeight() != BG_HEIGHT || background.getWidth() != BG_WIDTH) return;

                Graphics2D g2d = background.createGraphics();
                g2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(
                        RenderingHints.KEY_STROKE_CONTROL,
                        RenderingHints.VALUE_STROKE_PURE);
                g2d.setRenderingHint(
                        RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);

                drawTopRectangle(g2d);

                String welcomeMessage = (welcomeInfo[1]
                        .replaceAll("%user_mention%", "<@" + event.getUser().getId() + ">")
                        .replaceAll("%user_name%", event.getUser().getName())
                        .replaceAll("%user_tag%", event.getUser().getAsTag())
                        .replaceAll("%guild_name%", event.getGuild().getName()));

                String welcomeImageMessage = welcomeInfo[3]
                        .replaceAll("%user_mention%", "<@" + event.getUser().getId() + ">")
                        .replaceAll("%user_name%", event.getUser().getName())
                        .replaceAll("%user_tag%", event.getUser().getAsTag())
                        .replaceAll("%guild_name%", event.getGuild().getName());

                int TITLE_FONT_SIZE = 80;
                drawCenteredString(g2d, welcomeImageMessage,
                        BG_HEIGHT * 82 / 100, new Font("coolvetica rg", Font.PLAIN, TITLE_FONT_SIZE));
                int SUBTITLE_FONT_SIZE = 50;
                drawCenteredString(g2d, "member #" + event.getGuild().getMemberCount(),
                        BG_HEIGHT * 88 / 100, new Font("coolvetica rg", Font.PLAIN, SUBTITLE_FONT_SIZE));

                if (rawPfp.getHeight() != PFP_DIM)
                    pfp.getGraphics().drawImage(rawPfp.getScaledInstance(PFP_DIM, PFP_DIM, Image.SCALE_SMOOTH), 0, 0, null);
                else
                    pfp.getGraphics().drawImage(rawPfp, 0, 0, null);

                drawCenteredImage(g2d, pfp, (BG_HEIGHT - pfp.getHeight()) * 38 / 100);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(background, "jpg", baos);
                byte[] bytes = baos.toByteArray();

                 Objects.requireNonNull(event.getGuild().getTextChannelById(welcomeInfo[0])).sendMessage(welcomeMessage)
                        .addFile(bytes, "welcome_" + event.getUser().getName() + ".jpg").queue();

            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawCenteredString(Graphics g2d, String text, int textHeight, Font font) {
        Color color = new Color(255, 255, 255, 255);
        g2d.setColor(color);
        int metricsWidth = g2d.getFontMetrics(font).stringWidth(text);
        int fontSize = font.getSize();
        g2d.setFont(font);
        while (metricsWidth > BG_WIDTH - (MARGIN * 2)) { // 40px on each side
            fontSize -= 5;
            font = new Font(font.getFontName(), font.getStyle(), fontSize);
            g2d.setFont(font);
            metricsWidth = g2d.getFontMetrics(font).stringWidth(text);
        }
        int x = (BG_WIDTH - metricsWidth) / 2;
        g2d.drawString(text, x, textHeight);
    }

    public void drawCenteredImage(Graphics2D g2d, BufferedImage pfp, int foregroundHeight) {
        Color color = new Color(255, 255, 255, 255);
        g2d.setStroke(new BasicStroke(20));
        g2d.setColor(color);
        int x = (BG_WIDTH - PFP_DIM) / 2;
        g2d.drawArc((BG_WIDTH - PFP_DIM) / 2, foregroundHeight, PFP_DIM, PFP_DIM, 0, 360);
        g2d.setClip(new Ellipse2D.Double((BG_WIDTH - (double)PFP_DIM) / 2, foregroundHeight, PFP_DIM, PFP_DIM));
        g2d.drawImage(pfp, x, foregroundHeight, null);
    }

    public void drawTopRectangle(Graphics2D g2d) {
        Color color = new Color(99, 85, 91, 0x80); // i wonder if this is a nice color
        g2d.setColor(color);
        g2d.fillRect(MARGIN, MARGIN, BG_WIDTH - (2 * MARGIN), BG_HEIGHT - (2 * MARGIN));
    }

}
