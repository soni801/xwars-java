package com.xwars.main;

import javax.swing.*;
import java.awt.*;

/**
 * The <code>Window</code> class handles with creating a JFrame for the application
 * to open in
 *
 * @author soni801
 */

public class Window extends Canvas
{
    private static final long serialVersionUID = 1L;

    public JFrame frame;
    private static JFrame loading;

    public Window(int width, int height, String title, Game game)
    {
        frame = new JFrame(title);

        frame.setSize(width, height);

        game.setBounds(0, 0, width, height);

        loading.setVisible(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(game.icon);
        frame.setUndecorated(true);
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }

    public static void showLoading()
    {
        JLabel label = new JLabel();
        label.setText("Loading...");
        label.setFont(Game.font.deriveFont(30f));

        loading = new JFrame("Loading...");

        loading.setSize(200, 50);

        loading.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        loading.setResizable(false);
        loading.setLocationRelativeTo(null);
        loading.setUndecorated(true);
        loading.setAlwaysOnTop(true);
        loading.setLayout(new FlowLayout());
        loading.add(label);
        loading.setVisible(true);
    }
}