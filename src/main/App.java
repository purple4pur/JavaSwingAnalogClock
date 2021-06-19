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

    JLayeredPane mainPanel = new JLayeredPane();
    Dial dial;
    Pointer pointer;

    public App() {
        super("时钟");

        LocalTime time = LocalTime.now(); // 当前时间
        hour = time.getHour() % 12; // 只取 0 ~ 11
        minute = time.getMinute();
        second = time.getSecond();

        dial = new Dial(120);
        pointer = new Pointer(70, 90, 100);
        pointer.setTime(hour, minute, second);

        dial.setBounds(0, 0, width, height); // 在 JLayeredPane 中显示必须 setBounds
        pointer.setBounds(0, 0, width, height);
        mainPanel.add(dial, 1);
        mainPanel.add(pointer, 2);
        setContentPane(mainPanel);

        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        while (true) {
            try {
                Thread.sleep(1000); // 1s
            } catch (Exception ex) {}
            pointer.nextSecond();
            mainPanel.repaint(); // 重绘整个窗口
        }
    }
}

// 表盘
class Dial extends JPanel {
    int r; // 表盘半径
    final double radDelta = Math.toRadians(30); // 30°
    final Stroke boldStroke = new BasicStroke(2);
    final Stroke regularStroke = new BasicStroke(1);

    public Dial(int radius) {
        r = radius;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(142, 130); // 窗口中心 (?)

        // 外围圆形
        g2d.setStroke(boldStroke);
        g2d.drawOval(-r, -r, r*2, r*2);

        // 刻度线
        int len;
        for (int i = 0; i < 60; ++i) {
            // 长刻度线
            if (i % 5 == 0) {
                g2d.setStroke(boldStroke);
                len = 10;
            // 短刻度线
            } else {
                g2d.setStroke(regularStroke);
                len = 6;
            }
            g2d.drawLine(0, 2-r, 0, len-r);
            g2d.rotate(Math.toRadians(6));
        }

        // 数字
        g2d.setFont(new Font(null, Font.BOLD, 14));
        for (int i = 1; i <= 12; ++i) {
            g2d.drawString(
                String.valueOf(i),
                (int) (0.84 * r * Math.sin(radDelta * i) - 4), // 带位置修正
                (int) (0.84 * -r * Math.cos(radDelta * i) + 5)
            );
        }
    }
}

// 指针
class Pointer extends JPanel {
    int hourLen, minLen, secLen;
    double hour = 0;   // 0 ~ 60
    double minute = 0; // 0 ~ 60
    int second = 0;    // 0 ~ 60
    final double radDelta = Math.toRadians(6); // 6°
    final Stroke boldStroke = new BasicStroke(2);
    final Stroke regularStroke = new BasicStroke(1);

    public Pointer(int hourLength, int minuteLength, int secondLength) {
        hourLen = hourLength;
        minLen = minuteLength;
        secLen = secondLength;
    }

    public void setTime(int hour, int minute, int second) {
        this.second = second;
        this.minute = minute + this.second/60.0;
        this.hour = (hour + this.minute/60.0) * 5.0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // 清除上次显示

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(142, 130); // 窗口中心 (?)
        g2d.setStroke(boldStroke);

        // 时针
        g2d.setColor(Color.RED);
        g2d.drawLine(
            0,
            0,
            (int) (hourLen * Math.sin(radDelta * hour)),
            (int) (-hourLen * Math.cos(radDelta * hour))
        );

        // 分针
        g2d.setColor(Color.BLUE);
        g2d.drawLine(
            0,
            0,
            (int) (minLen * Math.sin(radDelta * minute)),
            (int) (-minLen * Math.cos(radDelta * minute))
        );

        // 秒针
        g2d.setColor(Color.GREEN);
        g2d.drawLine(
            0,
            0,
            (int) (secLen * Math.sin(radDelta * second)),
            (int) (-secLen * Math.cos(radDelta * second))
        );
    }

    public void nextSecond() {
        second += 1;
        if (second >= 60) {
            second = 0;
        }

        minute += 1.0/60.0;
        if (minute >= 60) {
            minute = 0;
        }

        hour += 1.0/60.0/60.0;
        if (hour >= 60) {
            hour = 0;
        }
    }
}
