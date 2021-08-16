package listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class JoinListener extends ListenerAdapter {

    String imagePath = "src/main/resources/images/discord_blank.jpg";

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        User user = event.getUser();

        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            Graphics2D img = image.createGraphics();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/coolvetica rg.ttf")));
            drawCenteredString(img, user.getAsTag() + " wants to be comfy :D", image.getWidth(), 8 * image.getHeight() / 10, new Font("coolvetica rg", Font.PLAIN, 70));
            drawCenteredString(img, "member #" + event.getGuild().getMemberCount(), image.getWidth(), 13 * image.getHeight() / 15, new Font("coolvetica rg", Font.PLAIN, 50));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            event.getGuild().getDefaultChannel().sendMessage("hi <@" + event.getUser().getId() + "> <3 welcome to yurahcomfy" +
                    " \u02DA \u0F18 \u2661 \u22C6\uFF61\u02DA\u2740").addFile(bytes, "welcome_" + event.getUser().getName() + ".jpg").queue();
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void drawCenteredString(Graphics g, String text, int imageWidth, int textHeight, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        int x = (imageWidth - metrics.stringWidth(text)) / 2;
        g.drawString(text, x, textHeight);
    }
}
