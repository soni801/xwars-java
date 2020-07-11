package com.xwars.main;

/*
 * Author: soni801
 */

import com.xwars.states.Settings;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas
{
    private static final long serialVersionUID = 1L;

    public JFrame frame;

    public Window(int width, int height, String title, Game game, Settings settings)
    {
        frame = new JFrame(title);

        frame.setSize(width, height);

        game.setBounds(0, 0, width, height);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(game.icon);
        frame.setUndecorated(true);
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }
}