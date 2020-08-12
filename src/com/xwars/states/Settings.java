package com.xwars.states;

import com.xwars.main.Game;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The <code>Settings</code> class is used when the application is in the Settings state,
 * as well as saving, loading and resetting settings on the hard drive using its
 * <code>save()</code>, <code>load()</code> and <code>reset()</code> methods
 *
 * @author soni801
 */

public class Settings
{
    private final Game game;

    public Map<String, String> settings = new HashMap<>();

    public int page = 1;

    public Settings(Game game)
    {
        this.game = game;
        
        settings.put("theme", "light");
        settings.put("resolution", "1280x720");
        settings.put("showfps", "false");
        settings.put("language", "en_US");
        settings.put("volume", "1.0");
    }

    public void save()
    {
        if (System.getProperty("os.name").toLowerCase().contains("win"))
        {
            try
            {
                FileOutputStream file = new FileOutputStream(System.getenv("AppData") + "\\" + Game.BRAND + "\\" + Game.PRODUCT + "\\" + "settings.xcfg");
                ObjectOutputStream out = new ObjectOutputStream(file);
        
                out.writeObject(settings);
        
                out.close();
                file.close();
        
                System.out.println("Saved settings");
            }
            catch (IOException e)
            {
                System.out.println("Failed to save settings.");
            }
        }
        else System.out.println("Unknown operating system. Cannot save settings.");
    }
    
    public void load()
    {
        if (System.getProperty("os.name").toLowerCase().contains("win"))
        {
            try
            {
                FileInputStream file = new FileInputStream(System.getenv("AppData") + "\\" + Game.BRAND + "\\" + Game.PRODUCT + "\\" + "settings.xcfg");
                ObjectInputStream in = new ObjectInputStream(file);
            
                settings = (Map<String, String>) in.readObject();
            
                in.close();
                file.close();
            
                System.out.println("Loaded settings");
            }
            catch (IOException | ClassNotFoundException e)
            {
                System.out.println("Failed to load settings.");
            }
        }
        else System.out.println("Unknown operating system. Cannot load settings.");
    }
    
    public void reset()
    {
        settings.put("theme", "light");
        settings.put("resolution", "1280x720");
        settings.put("showfps", "false");
        settings.put("language", "en_US");
        settings.put("volume", "1.0");
    }

    public void tick()
    {

    }

    public void render(Graphics g)
    {
        switch (settings.get("theme"))
        {
            case "light" : g.setColor(Color.BLACK); break;
            case "dark"  : g.setColor(Color.WHITE); break;
        }

        g.setFont(Game.font.deriveFont(60f));
        g.drawString(Game.BUNDLE.getString("settings.title"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(60f)).stringWidth(Game.BUNDLE.getString("settings.title")) / 2, Game.HEIGHT / 2 - 170);

        g.setFont(Game.font.deriveFont(40f));
        g.drawString(Game.BUNDLE.getString("settings.settings"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(40f)).stringWidth(Game.BUNDLE.getString("settings.settings")) / 2, Game.HEIGHT / 2 - 170 + 40);

        switch (settings.get("theme"))
        {
            case "light" : g.setColor(new Color(80, 80, 80));    break;
            case "dark"  : g.setColor(new Color(160, 160, 160)); break;
        }

        switch (page)
        {
            case 1:
                g.setFont(Game.font.deriveFont(30f));
                g.drawString(Game.BUNDLE.getString("settings.theme"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.theme")) / 2, Game.HEIGHT / 2 - 70);
                g.drawString(Game.BUNDLE.getString("settings.resolution"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.resolution")) / 2, Game.HEIGHT / 2 - 70 + 80);
                g.drawString(Game.BUNDLE.getString("settings.showfps"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.showfps")) / 2, Game.HEIGHT / 2 - 70 + 160);

                g.setColor(new Color(120, 120, 120));
                g.setFont(Game.font.deriveFont(20f));
                g.drawString(settings.get("theme"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(20f)).stringWidth(settings.get("theme")) / 2, Game.HEIGHT / 2 - 70 + 30);
                g.drawString(settings.get("resolution"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(20f)).stringWidth(settings.get("resolution")) / 2, Game.HEIGHT / 2 - 70 + 80 + 30);
                g.drawString(settings.get("showfps"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(20f)).stringWidth(settings.get("showfps")) / 2, Game.HEIGHT / 2 - 70 + 160 + 30);

                if (settings.get("theme").equals("dark")) g.drawImage(game.arrow_left, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 - 13, null);
                if (!settings.get("resolution").equals("960x540")) g.drawImage(game.arrow_left, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 + 80 - 13, null);
                if (settings.get("showfps").equals("true")) g.drawImage(game.arrow_left, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 + 160 - 13, null);

                if (settings.get("theme").equals("light")) g.drawImage(game.arrow_right, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70 - 13, null);
                if (!settings.get("resolution").equals("fullscreen")) g.drawImage(game.arrow_right, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70 + 80 - 13, null);
                if (settings.get("showfps").equals("false")) g.drawImage(game.arrow_right, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70 + 160 - 13, null);
                break;
            case 2:
                g.setFont(Game.font.deriveFont(30f));
                g.drawString(Game.BUNDLE.getString("settings.volume"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.volume")) / 2, Game.HEIGHT / 2 - 70);

                g.setColor(new Color(120, 120, 120));
                g.drawLine(Game.WIDTH / 2 - 200, Game.HEIGHT / 2 - 70 + 30, Game.WIDTH / 2 + 200, Game.HEIGHT / 2 - 70 + 30);
                g.drawLine(Game.WIDTH / 2 - 200, Game.HEIGHT / 2 - 70 + 30 - 1, Game.WIDTH / 2 + 200, Game.HEIGHT / 2 - 70 + 30 - 1);
                g.fillOval((int)(Game.WIDTH / 2 - 200 + (Float.parseFloat(settings.get("volume")) * 400) - 8), Game.HEIGHT / 2 - 70 + 30 - 8, 15, 15);
                break;
        }

        g.setColor(new Color(120, 120, 120));
        g.setFont(Game.font.deriveFont(20f));
        g.drawString(Game.BUNDLE.getString("settings.notice"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(20f)).stringWidth(Game.BUNDLE.getString("settings.notice")) / 2, Game.HEIGHT - 50 - 10 + 35 - 50 - 40);

        g.setFont(Game.font.deriveFont(30f));
        g.drawString(Game.BUNDLE.getString("settings.reset").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.reset").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 50);
        g.drawString(Game.BUNDLE.getString("settings.back").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35);

        if (page > 1) g.drawString("<", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<") / 2 - 100, Game.HEIGHT - 50 - 10 + 35);
        if (page < 2) g.drawString(">", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(">") / 2 + 100, Game.HEIGHT - 50 - 10 + 35);
    }
}