package Listeners;

import DB.MySQLInterfacer;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Objects;

public class MemberJoinListener extends ListenerAdapter {

    String imagePath = "src/main/resources/images/welcome_blank_0.jpg";

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String[] welcomeInfo = MySQLInterfacer.getWelcomeInfo(event.getGuild().getId());
        if (welcomeInfo[0] != null) {
            User user = event.getUser();
            try {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/coolvetica rg.ttf")));

                BufferedImage image = ImageIO.read(new File(imagePath));
                Graphics2D g2d = image.createGraphics();
                g2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(
                        RenderingHints.KEY_STROKE_CONTROL,
                        RenderingHints.VALUE_STROKE_PURE);
                g2d.setRenderingHint(
                        RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);

                drawCenteredString(g2d, user.getAsTag() + " wants to be comfy :D",
                        image.getWidth(), 8 * image.getHeight() / 10, new Font("coolvetica rg", Font.PLAIN, 80));
                drawCenteredString(g2d, "member #" + event.getGuild().getMemberCount(),
                        image.getWidth(), 13 * image.getHeight() / 15, new Font("coolvetica rg", Font.PLAIN, 50));

                BufferedImage pfp = ImageIO.read(new URL(user.getAvatarUrl() + "?size=512"));

                g2d.setStroke(new BasicStroke(20));
                drawCenteredImage(g2d, pfp, image.getWidth(), (image.getHeight() - pfp.getHeight()) / 2 - 60);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);
                byte[] bytes = baos.toByteArray();

                String welcomeMessage;

                if (welcomeInfo[1] == null)
                    welcomeInfo[1] = "welcome to %guild_name%, %user_mention%!";

                welcomeMessage = (welcomeInfo[1]
                        .replaceAll("%user_mention%", "<@" + event.getUser().getId() + ">")
                        .replaceAll("%user_name%", event.getUser().getName())
                        .replaceAll("%guild_name%", event.getGuild().getName()));

                Objects.requireNonNull(event.getGuild().getTextChannelById(welcomeInfo[0])).sendMessage(welcomeMessage)
                        .addFile(bytes, "welcome_" + event.getUser().getName() + ".jpg").queue();

            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawCenteredString(Graphics g2d, String text, int backgroundWidth, int textHeight, Font font) {
        int metricsWidth = g2d.getFontMetrics(font).stringWidth(text);
        int fontSize = font.getSize();
        g2d.setFont(font);
        while (metricsWidth > backgroundWidth - 80) { // i arbitrarily decided that 40px on each side is nice
            fontSize -= 5;
            font = new Font(font.getFontName(), font.getStyle(), fontSize);
            g2d.setFont(font);
            metricsWidth = g2d.getFontMetrics(font).stringWidth(text);
        }
        int x = (backgroundWidth - metricsWidth) / 2;
        g2d.drawString(text, x, textHeight);
    }

    public void drawCenteredImage(Graphics2D g2d, BufferedImage img, int backgroundWidth, int foregroundHeight) {
        int x = (backgroundWidth - img.getWidth()) / 2;
        g2d.drawArc(backgroundWidth/2-256, foregroundHeight, 512, 512, 0, 360);
        g2d.setClip(new Ellipse2D.Double((double)backgroundWidth/2-256, foregroundHeight, 512, 512));
        g2d.drawImage(img, x, foregroundHeight, null);
    }
}
