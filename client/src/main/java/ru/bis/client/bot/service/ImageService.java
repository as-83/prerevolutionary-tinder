package ru.bis.client.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ImageService {

    public static final String FONT_NAME = "Old Standard TT";
    public static final String IMG_FORMAT = "jpg";
    @Value("${background.img}")
    private String backgroundLocation;
    @Value("${image-service.images.location}")
    private String imagesLocation;
    private static final Map<String, String> imageCash = new ConcurrentHashMap<>();

    public String getImage(String description) {
        if (imageCash.containsKey(description)) {
            log.info("Getting image from cache");
            return imageCash.get(description);
        }

        log.debug("Image creation started");

        try {
            String header = description.substring(0, description.indexOf(" "));


            final BufferedImage image = ImageIO.read(new File(backgroundLocation));
            Font headerFont = new Font(FONT_NAME, Font.BOLD, 60 - header.length());
            Font descriptionFont = new Font(FONT_NAME, Font.BOLD, 45 - description.length() / 5);

            Graphics g = image.getGraphics();
            g.setFont(headerFont);
            g.setColor(Color.BLACK);
            g.drawString(header, 100, 100);
            g.dispose();

            if (header.length() < description.length()) {
                log.debug("Drawing the body of description");
                String body = description.substring(description.indexOf(" "));
                g = image.getGraphics();
                g.setFont(descriptionFont);
                g.setColor(Color.DARK_GRAY);
                g.drawString(body, 100, 200);
                g.dispose();
            }

            String fullImageName = imagesLocation + UUID.randomUUID() + "." + IMG_FORMAT;
            File file = new File(fullImageName);
            ImageIO.write(image, IMG_FORMAT, file);
            imageCash.put(description, fullImageName);
            log.debug("Image created. Location:" + fullImageName);

            return fullImageName;
        } catch (IOException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return backgroundLocation;
    }

}
