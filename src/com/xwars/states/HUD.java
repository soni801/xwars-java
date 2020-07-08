package com.xwars.states;

/*
 * Author: soni801
 */

import com.xwars.main.Game;
import com.xwars.main.Handler;
import com.xwars.gameobjects.Tile;

import java.awt.*;

public class HUD
{
    private Handler handler;
    private Customise customise;

    public int currentPlayer = 1;

    public HUD(Handler handler, Customise customise)
    {
        this.handler = handler;
        this.customise = customise;
    }

    public void generate(int w, int h)
    {
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                handler.addObject(new Tile(x * 25, y * 25, x, y, customise));
            }
        }
    }

    public void tick()
    {

    }

    public void render(Graphics g)
    {
        switch (Settings.settings.get("theme"))
        {
            case "light" : g.setColor(new Color(190, 190, 190)); break;
            case "dark"  : g.setColor(new Color(50, 50, 50));    break;
        }

        g.fillRect(0, Game.HEIGHT - 150 - 5, Game.WIDTH, 5);

        switch (Settings.settings.get("theme"))
        {
            case "light" : g.setColor(new Color(230, 230, 230)); break;
            case "dark"  : g.setColor(new Color(60, 60, 60));    break;
        }

        g.fillRect(0, Game.HEIGHT - 150, Game.WIDTH, 150);

        switch (Settings.settings.get("theme"))
        {
            case "light" : g.setColor(Color.BLACK); break;
            case "dark"  : g.setColor(Color.WHITE); break;
        }

        g.setFont(Game.font.deriveFont(30f));
        g.drawString("Current player:", 20, Game.HEIGHT - 150 + 20 + 25);

        g.setFont(Game.font.deriveFont(20f));
        g.drawString(customise.playerName[currentPlayer - 1], 20, Game.HEIGHT - 20);

        g.setColor(customise.playerColor[currentPlayer - 1]);
        g.fillRect(20, Game.HEIGHT - 20 - 25 - 30, 30, 30);

        if (Game.PAUSED)
        {
            switch (Settings.settings.get("theme"))
            {
                case "light" : g.setColor(new Color(1, 1, 1, .6f)); break;
                case "dark"  : g.setColor(new Color(0, 0, 0, .3f)); break;
            }

            g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

            switch (Settings.settings.get("theme"))
            {
                case "light" : g.setColor(Color.BLACK); break;
                case "dark"  : g.setColor(Color.WHITE); break;
            }

            g.setFont(Game.font.deriveFont(70f));
            g.drawString("PAUSED", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(70f)).stringWidth("PAUSED") / 2, Game.HEIGHT / 2 - 150);

            g.setFont(Game.font.deriveFont(30f));
            g.drawString("Continue", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Continue") / 2, Game.HEIGHT / 2 - 90);
            g.drawString("Menu", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Menu") / 2, Game.HEIGHT / 2 - 60);
            g.drawString("Exit", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Exit") / 2, Game.HEIGHT / 2 - 30);

            // g.drawRect(Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Continue") / 2, Game.HEIGHT / 2 - 90 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Continue"), 20);
            // g.drawRect(Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Menu") / 2, Game.HEIGHT / 2 - 60 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Menu"), 20);
            // g.drawRect(Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Exit") / 2, Game.HEIGHT / 2 - 30 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Exit"), 20);
        }
    }
}