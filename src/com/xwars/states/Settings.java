package com.xwars.states;

import com.xwars.main.Game;

import java.awt.*;
import java.io.*;
import java.util.*;

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

    public static String osname;
    public static String userhome;
    public static String environment;
    public static String javaversion;

    public static Map<String, String> settings = new HashMap<>();

    public int page = 1;

    public Settings(Game game)
    {
        this.game = game;
        
        System.out.println("Collecting system info...");
    
        osname = System.getProperty("os.name");
        userhome = System.getProperty("user.home");
        environment = Game.class.getResource("Game.class").toString();
        javaversion = System.getProperty("java.version");
    
        environment = environment.startsWith("jar") ? "JAR" : "IDE";
    }

    public void save()
    {
        new com.xwars.main.File().save("settings.xcfg", "theme=" + settings.get("theme") + "\n" +
                "resolution=" + settings.get("resolution") + "\n" +
                "showfps=" + settings.get("showfps") + "\n" +
                "language=" + settings.get("language") + "\n" +
                "volume=" + settings.get("volume") + "\n");
    }

    public void load()
    {
        String workingDirectory;
        String brand = com.xwars.main.File.BRAND;
        String product = com.xwars.main.File.PRODUCT;
        
        System.out.println("System info:");
        System.out.println("\tOperating System: " + osname);
        System.out.println("\tUser Home Directory: " + userhome);
        System.out.println("\tEnvironment: " + environment);
        System.out.println("\tJava Version: " + javaversion);
    
        if (osname.toLowerCase().contains("win"))
        {
            workingDirectory = System.getenv("AppData");
            String absolutePath = workingDirectory + "\\" + brand + "\\" + product + "\\";
            String path = absolutePath + "settings.xcfg";
    
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path)))
            {
                String line = bufferedReader.readLine();
                while(line != null)
                {
                    if (line.contains("="))
                    {
                        String key = line;
                        String value = line;
                
                        while (key.contains("="))
                        {
                            key = key.substring(0, key.length() - 1);
                        }
                
                        while (value.contains("="))
                        {
                            value = value.substring(1);
                        }
                
                        settings.put(key, value);
                    }
            
                    line = bufferedReader.readLine();
                }
                System.out.println("Loaded settings: " + settings);
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Settings File not found. Trying to create default file.");
                reset();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else System.out.println("Unknown operating system. Cannot load settings.");
    }

    public void reset()
    {
        new com.xwars.main.File().save("settings.xcfg", "theme=light\n" +
                "resolution=1280x720\n" +
                "showfps=false\n" +
                "language=en_US\n" +
                "volume=1.0\n");
        
        load();
    }

    public void tick()
    {

    }

    public void render(Graphics g)
    {
        switch (Settings.settings.get("theme"))
        {
            case "light" : g.setColor(Color.BLACK); break;
            case "dark"  : g.setColor(Color.WHITE); break;
        }

        g.setFont(Game.font.deriveFont(60f));
        g.drawString(Game.BUNDLE.getString("settings.title"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(60f)).stringWidth(Game.BUNDLE.getString("settings.title")) / 2, Game.HEIGHT / 2 - 170);

        g.setFont(Game.font.deriveFont(40f));
        g.drawString(Game.BUNDLE.getString("settings.settings"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(40f)).stringWidth(Game.BUNDLE.getString("settings.settings")) / 2, Game.HEIGHT / 2 - 170 + 40);

        switch (Settings.settings.get("theme"))
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
                if (settings.get("resolution").equals("1280x720") || settings.get("resolution").equals("1600x900")) g.drawImage(game.arrow_left, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 + 80 - 13, null);
                if (settings.get("showfps").equals("true")) g.drawImage(game.arrow_left, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 + 160 - 13, null);

                if (settings.get("theme").equals("light")) g.drawImage(game.arrow_right, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70 - 13, null);
                if (settings.get("resolution").equals("960x540") || settings.get("resolution").equals("1280x720")) g.drawImage(game.arrow_right, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70 + 80 - 13, null);
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