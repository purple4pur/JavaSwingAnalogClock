import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.time.*;
 
public class App extends JFrame {
    public static void main(String[] args) {
        new App();
        LocalTime t = LocalTime.now(); // 当前时间
        System.out.println(t);
    }

    int width, height;
    int hour, minute, second;

    JPanel dial;

    public App() {
        super("时钟");

        LocalTime time = LocalTime.now();
        hour = time.getHour() % 12;
        minute = time.getMinute();
        second = time.getSecond();

        width = height = 300;

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
    public Dial(int radius) {
        r = radius;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(142, 130); // ?
        g2d.drawOval(-r, -r, r*2, r*2);
    }
}
