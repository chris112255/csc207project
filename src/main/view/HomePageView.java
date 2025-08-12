package main.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HomePageView extends JPanel {
    private final JLabel ascii = new JLabel("", SwingConstants.CENTER);
    private Timer timer;

    public HomePageView(Runnable goExplore, Runnable goSaved, Runnable goPlanner) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Recipe Manager", SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 4, 0));
        add(title, BorderLayout.NORTH);

        // center the ASCII perfectly
        JPanel center = new JPanel(new GridBagLayout());
        ascii.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));
        ascii.setHorizontalAlignment(SwingConstants.CENTER);
        center.add(ascii, new GridBagConstraints());
        add(center, BorderLayout.CENTER);

        startAsciiAnimation();
    }

    private void startAsciiAnimation() {
        List<String> frames = buildFrames();
        if (timer != null) timer.stop();
        final int[] i = {0};
        timer = new Timer(220, e -> {
            ascii.setText(frames.get(i[0]));  // <-- fixed extra bracket
            i[0] = (i[0] + 1) % frames.size();
        });
        timer.start();
    }

    private List<String> buildFrames() {
        ArrayList<String> frames = new ArrayList<>();

        // steam wobble
        String steamA =
                "                 (  )                   \n" +
                        "               (      )                 \n" +
                        "                 (  )        ~          \n";
        String steamB =
                "                   (  )                 \n" +
                        "                 (      )   ~           \n" +
                        "                   (  )                 \n";
        String steamC =
                "                 ( ) ( )                \n" +
                        "                   (  )       ~         \n";

        // pot with handles, blank body
        String pot =
                        "              _____________              \n" +
                        "        _____/             \\____        \n" +
                        "       |                         |       \n" +
                        "       |                         |       \n" +
                        "       |_________________________|       \n" +
                        "         \\___________________/          \n" +
                        "                 sizzle...               ";

        frames.add(pre(steamA + pot));
        frames.add(pre(steamB + pot));
        frames.add(pre(steamC + pot));
        frames.add(pre(steamB + pot));

        return frames;
    }

    private String pre(String body) {
        return "<html><div style='text-align:center'>"
                + "<pre style='display:inline-block; text-align:left; "
                + "font-family:monospace; font-size:22px; line-height:1.05; margin:0'>"
                + body
                + "</pre></div></html>";
    }
}
