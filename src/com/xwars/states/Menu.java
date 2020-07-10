package com.xwars.states;

/*
 * Author: soni801
 */

import com.xwars.main.Game;

import java.awt.*;

public class Menu
{
    private boolean splash = true;
    private float time = 0;

    private Game game;

    public Menu(Game game)
    {
        this.game = game;
    }

    public void tick()
    {
        if (splash) time++;
    }

    public void render(Graphics g)
    {
        switch (Settings.settings.get("theme"))
        {
            case "light" : g.setColor(Color.BLACK); break;
            case "dark"  : g.setColor(Color.WHITE); break;
        }

        g.setFont(Game.font.deriveFont(60f));
        g.drawString(Game.BUNDLE.getString("menu.title"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(60f)).stringWidth(Game.BUNDLE.getString("menu.title")) / 2, Game.HEIGHT / 2 - 170);

        g.setColor(new Color(120, 120, 120));
        g.setFont(Game.font.deriveFont(30f));
        g.drawString(Game.BUNDLE.getString("menu.play").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("menu.play").toUpperCase()) / 2, Game.HEIGHT - 220);
        g.drawString(Game.BUNDLE.getString("menu.settings").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("menu.settings").toUpperCase()) / 2, Game.HEIGHT - 220 + 60);
        g.drawString(Game.BUNDLE.getString("menu.quit").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("menu.quit").toUpperCase()) / 2, Game.HEIGHT - 220 + 120);

        if (splash)
        {
            if (time < 200)
            {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

                g.drawImage(game.redsea, (int)(Game.WIDTH / 2 - (Game.WIDTH * game.redsea.getWidth() / (Game.WIDTH * 4)) / 2 - time), (int)(Game.HEIGHT / 2 - (Game.HEIGHT * game.redsea.getHeight() / (Game.HEIGHT * 4)) / 2 - time), (int)(Game.WIDTH * game.redsea.getWidth() / (Game.WIDTH * 4) + time * 2), (int)(Game.HEIGHT * game.redsea.getHeight() / (Game.HEIGHT * 4) + time * 2), null);
            }

            if (time < 50)
            {
                g.setColor(new Color(0, 0, 0, 1 - time / 50));
                g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            }
            else if (time > 150 && time <= 200)
            {
                g.setColor(new Color(0, 0, 0, (time - 150) / 50));
                g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            }
            else if (time >= 200 && time < 250)
            {
                g.setColor(new Color(0, 0, 0, 1 - (time - 200) / 50));
                g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            }
            else if (time > 250) splash = false;
        }
    }
}