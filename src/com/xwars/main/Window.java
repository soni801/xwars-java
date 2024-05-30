package com.xwars.main;

import javax.swing.*;
import java.awt.*;

/**
 * Handles creating a JFrame for the application to open in
 *
 * @author Soni
 * @version 1.1
 */
public class Window extends Canvas
{
    public final JFrame frame;
    private static JFrame loading;
    
    /**
     * Constructor. This creates a JFrame, and may only be instantiated once
     *
     * @param width Width of the JFrame
     * @param height Height of the JFrame
     * @param title Title of the JFrame
     * @param game The current Game object
     */
    public Window(int width, int height, String title, Game game)
    {
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        
        frame = new JFrame(title);

        if (width == 1)
        {
            Game.WIDTH = resolution.width;
            Game.HEIGHT = resolution.height;
            
            frame.setSize(resolution.width, resolution.height);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        else frame.setSize(width, height);

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
    
    /**
     * Opens the loading window before the application is fully launched
     */
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