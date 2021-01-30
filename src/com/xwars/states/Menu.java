package com.xwars.states;

import com.xwars.main.Game;
import com.xwars.main.loaders.ResourceLoader;

import java.awt.*;

/**
 * Used when the application is in the menu state
 *
 * @author Soni
 * @version 1
 */
public class Menu
{
    private final Game game;
    private final Settings settings;
    
    private boolean splash = true;
    private float time = 0;
    
    /**
     * Constructor
     *
     * @param game The current Game object
     * @param settings Instance of the Settings class
     */
    public Menu(Game game, Settings settings)
    {
        this.game = game;
        this.settings = settings;
    }
    
    /**
     * Executes 60 times a second
     */
    public void tick()
    {
        if (splash) time++;
    }
    
    /**
     * Renders objects to the screen
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
        g.drawString(ResourceLoader.nameOf("menu.title"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(60f)).stringWidth(ResourceLoader.nameOf("menu.title")) / 2, Game.HEIGHT / 2 - 170);

        g.setColor(new Color(120, 120, 120));
        g.setFont(Game.font.deriveFont(30f));
        g.drawString(ResourceLoader.nameOf("menu.play").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(ResourceLoader.nameOf("menu.play").toUpperCase()) / 2, Game.HEIGHT - 220 - 60);
        g.drawString(ResourceLoader.nameOf("menu.rules").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(ResourceLoader.nameOf("menu.rules").toUpperCase()) / 2, Game.HEIGHT - 220);
        g.drawString(ResourceLoader.nameOf("menu.settings").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(ResourceLoader.nameOf("menu.settings").toUpperCase()) / 2, Game.HEIGHT - 220 + 60);
        g.drawString(ResourceLoader.nameOf("menu.quit").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(ResourceLoader.nameOf("menu.quit").toUpperCase()) / 2, Game.HEIGHT - 220 + 120);

        // TODO: Find better solution for splash screen
        
        if (splash)
        {
            if (time < 200)
            {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

                //g.drawImage(game.redsea, (int)(Game.WIDTH / 2 - (Game.WIDTH * game.redsea.getWidth() / (Game.WIDTH * 4)) / 2 - time), (int)(Game.HEIGHT / 2 - (Game.HEIGHT * game.redsea.getHeight() / (Game.HEIGHT * 4)) / 2 - time), (int)(Game.WIDTH * game.redsea.getWidth() / (Game.WIDTH * 4) + time * 2), (int)(Game.HEIGHT * game.redsea.getHeight() / (Game.HEIGHT * 4) + time * 2), null);
                g.drawImage(game.redsea, Game.WIDTH / 2 - game.redsea.getWidth() / 3 / 2, Game.HEIGHT / 2 - game.redsea.getHeight() / 3 / 2, game.redsea.getWidth() / 3, game.redsea.getHeight() / 3, null);
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