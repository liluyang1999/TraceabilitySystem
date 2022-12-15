package com.example.common_resource.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * Utility of verification code
 */
public class VerCodeUtils {

    // Remove the confused characters(1, 0, i, o)
    public static final String VER_CODES = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";

    private static final Random random = new SecureRandom();

    public static String genVerCodes(int size) {
        return genVerCodes(size, VER_CODES);
    }

    public static String genVerCodes(int size, String sources) {
        if (sources == null || sources.length() == 0)
        {
            sources = VER_CODES;
        }

        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verCodes = new StringBuilder(size);
        for (int i = 0; i < size; i++)
        {
            int randPos = rand.nextInt(codesLen - 1);
            verCodes.append(sources.charAt(randPos));
        }
        return verCodes.toString();
    }

    public static void outputImage(int w, int h, OutputStream os, String code) throws IOException {
        int verSize = code.length();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();

        Graphics2D graphic = image.createGraphics();
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color[] colorSpace = new Color[] { Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY,
                                            Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW };
        Color[] colors = new Color[5];

        float[] fractions = new float[colors.length];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorSpace[rand.nextInt(colorSpace.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);

        graphic.setColor(Color.GRAY);   // border color
        graphic.fillRect(0, 0, w, h);

        Color c = getRandColor(200, 250);
        graphic.setColor(c); // background color
        graphic.fillRect(0, 2, w, h - 4);

        // draw disturbing lines
        Random random = new Random();
        graphic.setColor(getRandColor(160, 200)); // color of line
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(w - 1);
            int y = random.nextInt(h - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            graphic.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        // noise point
        float yawpRate = 0.05f; // noise rate
        int area = (int) (yawpRate * w * h);
        for (int i = 0; i < area; i++)
        {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int rgb = getRandColorByInt();
            image.setRGB(x, y, rgb);
        }

        shear(graphic, w, h, c); // distort the graphic

        graphic.setColor(getRandColor(100, 160));
        int fontSize = h - 4;
        Font font = new Font("Algerian", Font.ITALIC, fontSize);
        graphic.setFont(font);
        char[] chars = code.toCharArray();
        for (int i = 0; i < verSize; i++)
        {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (w / verSize) * i + fontSize / 2, h / 2);
            graphic.setTransform(affine);
            graphic.drawChars(chars, i, 1, ((w - 10) / verSize) * i + 5, h / 2 + fontSize / 2 - 10);
        }

        graphic.dispose();
        ImageIO.write(image, "jpg", os);
    }

    private static void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    private static void shearX(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(2);
        int phase = random.nextInt(2);
        int frames = 1;
        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            g.setColor(color);
            g.drawLine((int) d, i, 0, i);
            g.drawLine((int) d + w1, i, w1, i);
        }
    }

    private static void shearY(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(50);
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            g.setColor(color);
            g.drawLine(i, (int) d, i, 0);
            g.drawLine(i, (int) d + h1, i, h1);
        }
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static int getRandColorByInt() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb)
        {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++)
        {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }
}