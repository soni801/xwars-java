package com.xwars.states;

import com.xwars.main.Game;
import com.xwars.main.loaders.ResourceLoader;

import java.awt.*;

/**
 * Used when the application is in the Win state
 *
 * @author Soni
 * @version 1
 */
public class Win
{
    private final Settings settings;
    private final Customise customise;
    
    public int winner;
    
    /**
     * Constructor
     *
     * @param settings Instance of the Settings class
     * @param customise Instance of the Customise class
     */
    public Win(Settings settings, Customise customise)
    {
        this.settings = settings;
        this.customise = customise;
    }
    
    /**
     * Render objects to screen
     *
     * @param g Graphics object used for rendering
     */
    public void render(Graphics g)
    {
        switch (settings.get("theme"))
        {
            case "light" -> g.setColor(Color.BLACK);
            case "dark"  -> g.setColor(Color.WHITE);
        }
    
        g.setFont(Game.font.deriveFont(60f));
        g.drawString(customise.playerName[winner] + " " + ResourceLoader.nameOf("win.won"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(60f)).stringWidth(customise.playerName[winner] + " " + ResourceLoader.nameOf("win.won")) / 2, Game.HEIGHT / 2 - 170);
    
        g.setColor(new Color(120, 120, 120));
        g.setFont(Game.font.deriveFont(30f));
        g.drawString(ResourceLoader.nameOf("win.menu").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(ResourceLoader.nameOf("win.menu").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35);
    }
}