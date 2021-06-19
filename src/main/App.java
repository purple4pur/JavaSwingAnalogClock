package main;

import java.awt.*;
import javax.swing.*;
import java.time.*;

public class App extends JFrame {
    public static void main(String[] args) {
        new App();
        LocalTime t = LocalTime.now(); // 当前时间
        System.out.println(t);
    }

    final int width = 300;
    final int height = 300;
    int hour, minute, second;

    JPanel dial;

    public App() {
        super("时钟");

        LocalTime time = LocalTime.now();
        hour = time.getHour() % 12;
        minute = time.getMinute();
        second = time.getSecond();

        dial = new Dial(120);
        add(dial);

        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

class Dial extends JPanel {
    int r;
    final double radDelta = Math.toRadians(30); // 30°

    public Dial(int radius) {
        r = radius;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(142, 130); // ?

        Stroke boldStroke = new BasicStroke(2);
        Stroke regularStroke = new BasicStroke(1);
        g2d.setStroke(boldStroke);
        g2d.drawOval(-r, -r, r*2, r*2);
        
        int len;
        for (int i = 0; i < 60; ++i) {
            if (i % 5 == 0) {
                g2d.setStroke(boldStroke);
                len = 10;
            } else {
                g2d.setStroke(regularStroke);
                len = 6;
            }
            g2d.drawLine(0, 2-r, 0, len-r);
            g2d.rotate(Math.toRadians(6));
        }

        g2d.setFont(new Font(null, Font.BOLD, 14));
        for (int i = 1; i <= 12; ++i) {
            g2d.drawString(
                String.valueOf(i),
                (int) (0.84 * r * Math.sin(radDelta * i) - 4),
                (int) (0.84 * -r * Math.cos(radDelta * i) + 5)
            );
        }
    }
}
