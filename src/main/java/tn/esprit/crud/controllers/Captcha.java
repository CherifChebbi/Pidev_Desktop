package tn.esprit.crud.controllers;

import javafx.scene.image.Image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Captcha {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 80;
    private static final int FONT_SIZE = 40;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // Génère une chaîne aléatoire de longueur donnée
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    // Génère une image CAPTCHA avec des lettres aléatoires
    public static BufferedImage generateCaptchaImage() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // Remplir le fond avec une couleur aléatoire
        g.setColor(randomColor());
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Dessiner les lettres aléatoires
        String text = generateRandomString(6);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));
        g.drawString(text, 20, HEIGHT / 2);

        g.dispose();
        return image;
    }

    // Génère une couleur aléatoire
    private static Color randomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public static void main(String[] args) {
        BufferedImage captchaImage = generateCaptchaImage();
        ImageIcon icon = new ImageIcon(captchaImage);
        JLabel label = new JLabel(icon);

        JFrame frame = new JFrame("CAPTCHA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }


}
