package com.xwars.states;

/*
 * Author: soni801
 */

import com.xwars.main.Game;

import java.awt.*;

public class Menu
{
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
        g.drawString("The Great X Wars", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(60f)).stringWidth("The Great X Wars") / 2, Game.HEIGHT / 2 - 170);

        g.setColor(new Color(120, 120, 120));
        g.setFont(Game.font.deriveFont(30f));
        g.drawString("PLAY", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("PLAY") / 2, Game.HEIGHT - 220);
        g.drawString("SETTINGS", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("SETTINGS") / 2, Game.HEIGHT - 220 + 60);
        g.drawString("QUIT", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("QUIT") / 2, Game.HEIGHT - 220 + 120);
    }
}