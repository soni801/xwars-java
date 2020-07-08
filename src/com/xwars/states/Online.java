package com.xwars.states;

/*
 * Author: soni801
 */

import com.xwars.main.Game;

import java.awt.*;

public class Online
{
    public void tick()
    {

    }

    public void render(Graphics g)
    {
        g.setColor(new Color(120, 120, 120));
        g.setFont(Game.font.deriveFont(30f));

        g.drawString("MENU", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("MENU") / 2, Game.HEIGHT - 50 - 10 + 35);
        g.drawString("START", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("START") / 2, Game.HEIGHT - 50 - 10 - 50 - 10 + 35);
        g.drawString("PLAY OFFLINE", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("PLAY OFFLINE") / 2, Game.HEIGHT - 50 - 10 - 50 - 10 - 50 - 10 + 35);

    }
}