package com.lstar;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class HarlemShake {

    HarlemShake() throws IOException, InterruptedException {

        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sh.wav"));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + "sh.wav");
                }
            }
        }).start();

        ArrayList<String> sh = new ArrayList<>();
        sh.add("///////////////");
        sh.add("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        sh.add("///////////////");
        sh.add("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        sh.add("Con los");
        sh.add("Terroristas");
        sh.add("//////////");
        sh.add("\\\\\\\\\\\\\\\\\\\\");
        sh.add("Do the");
        sh.add("Harlem");
        sh.add("Shake");
        sh.add("//////////");
        sh.add("Ey (Ey)");
        sh.add("//////////");
        sh.add("Ey (Ey)");
        sh.add("//////////");
        sh.add("Ey (Ey)");
        sh.add("//////////");
        sh.add("\\\\\\\\\\\\\\\\\\\\");
        sh.add("Ey (Ey)");
        sh.add("//////////");
        sh.add("Ey (Ey)");
        sh.add("//////////");
        sh.add("Ey (Ey)");
        sh.add("//////////");
        sh.add("Do the");
        sh.add("Harlem");
        sh.add("Shake");
        sh.add("//////////");
        sh.add("\\\\\\\\\\\\\\\\\\\\");
        sh.add("Ey (Ey)");
        sh.add("//////////");
        sh.add("Do the");
        sh.add("Harlem");
        sh.add("Shake");




        for(String elem:sh) {
            System.out.print("\033[H\033[2J");
            BufferedImage image = new BufferedImage(144, 32, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.setFont(new Font("Dialog", Font.PLAIN, 24));
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.drawString(elem, 6, 24);
            //ImageIO.write(image, "png", new File("text.png"));

            for (int y = 0; y < 32; y++) {
                StringBuilder sb = new StringBuilder();
                for (int x = 0; x < 144; x++)
                    sb.append(image.getRGB(x, y) == -16777216 ? " " : image.getRGB(x, y) == -1 ? "#" : "*");
                if (sb.toString().trim().isEmpty()) continue;
                System.out.println(sb);
            }
            sleep(1000);
        }
    }
}
