package com.xwars.main;

/*
 * Author: soni801
 */

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas
{
    private static final long serialVersionUID = 1L;

    public Window(int width, int height, String title, Game game, Settings settings)
    {
        JFrame frame = new JFrame(title);

        switch (settings.environment)
        {
            case "IDE" : frame.setSize(width + 6, height + 29); break;
            case "JAR" : frame.setSize(width + 16, height + 39); break;
        }

        game.setBounds(0, 0, width, height);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(game.icon);
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }
}