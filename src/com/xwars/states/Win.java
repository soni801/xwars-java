package com.xwars.states;

import com.xwars.main.Game;

import java.awt.*;

/**
 * The <code>Win</code> class *DESCRIPTION*
 *
 * @author soni801
 */

public class Win
{
    private final Settings settings;
    private final Customise customise;
    
    public int winner;
    
    public Win(Settings settings, Customise customise)
    {
        this.settings = settings;
        this.customise = customise;
    }
    
    public void tick()
    {
    
    }
    
    public void render(Graphics g)
    {
        switch (settings.settings.get("theme"))
        {
            case "light" : g.setColor(Color.BLACK); break;
            case "dark"  : g.setColor(Color.WHITE); break;
        }
    
        g.setFont(Game.font.deriveFont(60f));
        g.drawString(customise.playerName[winner] + " " + Game.BUNDLE.getString("win.won"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(60f)).stringWidth(customise.playerName[winner] + " " + Game.BUNDLE.getString("win.won")) / 2, Game.HEIGHT / 2 - 170);
    
        g.setColor(new Color(120, 120, 120));
        g.setFont(Game.font.deriveFont(30f));
        g.drawString(Game.BUNDLE.getString("win.menu").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("win.menu").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35);
    }
}