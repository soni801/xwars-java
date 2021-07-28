package com.xwars.states;

import com.xwars.main.Game;
import com.xwars.main.loaders.ResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used when the application is in the rules state
 *
 * @author Soni
 * @version 1
 */
public class Rules
{
    private static final Logger LOGGER = Logger.getLogger(Rules.class.getName());
    
    private final Settings settings;
    
    public String rules;
    public int offset;
    
    /**
     * Constructor
     *
     * @param settings Instance of the Settings class
     */
    public Rules(Settings settings)
    {
        this.settings = settings;
        
        LOGGER.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        LOGGER.addHandler(consoleHandler);
        
        // TODO: Make rules local
        
        try
        {
            String webPage = "https://raw.githubusercontent.com/soni801/xwars/master/rules.txt";
            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuilder sb = new StringBuilder();
            while ((numCharsRead = isr.read(charArray)) > 0)
            {
                sb.append(charArray, 0, numCharsRead);
            }
            rules = sb.toString();
            LOGGER.info("Loaded rules");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Renders objects to screen
     *
     * @param g Graphics object used for rendering
     */
    public void render(Graphics g)
    {
        g.setFont(Game.font.deriveFont(20f));
        switch (settings.get("theme"))
        {
            case "light" -> g.setColor(new Color(80, 80, 80));
            case "dark"  -> g.setColor(new Color(160, 160, 160));
        }

        drawString(g, rules, Game.HEIGHT / 2 - 170 + 40 + 50 - offset);
    
        switch (settings.get("theme"))
        {
            case "light" -> g.setColor(Color.WHITE);
            case "dark"  -> g.setColor(Color.DARK_GRAY);
        }

        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT / 2 - 170 + 40 + 50 - 20);
        g.fillRect(0, Game.HEIGHT - 50 - 10 - 20, Game.WIDTH, 50 + 10 + 20);
    
        switch (settings.get("theme"))
        {
            case "light" -> g.setColor(Color.BLACK);
            case "dark"  -> g.setColor(Color.WHITE);
        }

        g.setFont(Game.font.deriveFont(60f));
        g.drawString(ResourceLoader.nameOf("rules.title"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(60f)).stringWidth(ResourceLoader.nameOf("rules.title")) / 2, Game.HEIGHT / 2 - 170);

        g.setFont(Game.font.deriveFont(40f));
        g.drawString(ResourceLoader.nameOf("rules.rules"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(40f)).stringWidth(ResourceLoader.nameOf("rules.rules")) / 2, Game.HEIGHT / 2 - 170 + 40);

        g.setColor(new Color(120, 120, 120));
        g.setFont(Game.font.deriveFont(30f));
        g.drawString(ResourceLoader.nameOf("rules.back").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(ResourceLoader.nameOf("rules.back").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35);

    }
    
    /**
     * Render a String to the screen
     *
     * @param g Graphics object used for rendering
     * @param text String to render
     * @param y Vertical rendering position
     */
    private void drawString(Graphics g, String text, int y)
    {
        for (String line : text.split("\n")) g.drawString(line, 10, y += g.getFontMetrics().getHeight());
    }
}