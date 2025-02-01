package edu.foobar.views;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MenuView extends JFrame {

    private JFrame frame;
    private JPanel panel;

    public MenuView() {
        frame = new JFrame("Menu");
        panel = new JPanel();
        frame.add(panel);
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void showMenu() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MenuView();
            }
        });
    }
}
