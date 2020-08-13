package com.xwars.states;

import com.xwars.gameobjects.Tile;
import com.xwars.main.Game;
import com.xwars.main.Handler;
import com.xwars.main.State;

import java.awt.*;

/**
 * The <code>HUD</code> class is used when the application is in the HUD state
 *
 * @author soni801
 */

public class HUD
{
    private final Handler handler;
    private final Customise customise;
    private final Settings settings;
    private final Game game;
    private final Win win;

    public int currentPlayer = 1;
    public boolean active;

    public HUD(Handler handler, Customise customise, Settings settings, Game game, Win win)
    {
        this.handler = handler;
        this.customise = customise;
        this.settings = settings;
        this.game = game;
        this.win = win;
    }

    public void generate(int w, int h)
    {
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                handler.tiles[x][y] = new Tile(x * 25, y * 25, x, y, customise, settings, this);
            }
        }
    }
    
    public void initialise()
    {
        active = true;
        
        for (Tile[] tiles : handler.tiles)
        {
            for (Tile tile : tiles)
            {
                tile.highlighted = false;
            
                if (tile.player == 0)
                {
                    try
                    {
                        if (handler.tiles[tile.posX - 1][tile.posY - 1].player == currentPlayer) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
                
                    try
                    {
                        if (handler.tiles[tile.posX - 1][tile.posY].player == currentPlayer) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
                
                    try
                    {
                        if (handler.tiles[tile.posX - 1][tile.posY + 1].player == currentPlayer) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
                
                    try
                    {
                        if (handler.tiles[tile.posX][tile.posY - 1].player == currentPlayer) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
                
                    try
                    {
                        if (handler.tiles[tile.posX][tile.posY + 1].player == currentPlayer) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
                
                    try
                    {
                        if (handler.tiles[tile.posX + 1][tile.posY - 1].player == currentPlayer) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
                
                    try
                    {
                        if (handler.tiles[tile.posX + 1][tile.posY].player == currentPlayer) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
                
                    try
                    {
                        if (handler.tiles[tile.posX + 1][tile.posY + 1].player == currentPlayer) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
                }
            }
        }
    }
    
    public void changePlayer()
    {
        currentPlayer++;
        if (currentPlayer > 2) currentPlayer = 1;
        
        boolean[][] invaded = new boolean[2][4];
        
        for (Tile[] tiles : handler.tiles)
        {
            for (Tile tile : tiles)
            {
                tile.highlighted = false;
                
                if (tile.invaded) invaded[tile.player - 1][tile.foundation - 1] = true;
                
                if (tile.player == 0 || (tile.foundation != 0 && tile.player != currentPlayer))
                {
                    try
                    {
                        if (handler.tiles[tile.posX - 1][tile.posY - 1].player == currentPlayer || (handler.tiles[tile.posX - 1][tile.posY - 1].player != currentPlayer && handler.tiles[tile.posX - 1][tile.posY - 1].invaded)) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
    
                    try
                    {
                        if (handler.tiles[tile.posX - 1][tile.posY].player == currentPlayer || (handler.tiles[tile.posX - 1][tile.posY].player != currentPlayer && handler.tiles[tile.posX - 1][tile.posY].invaded)) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
    
                    try
                    {
                        if (handler.tiles[tile.posX - 1][tile.posY + 1].player == currentPlayer || (handler.tiles[tile.posX - 1][tile.posY + 1].player != currentPlayer && handler.tiles[tile.posX - 1][tile.posY + 1].invaded)) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
    
                    try
                    {
                        if (handler.tiles[tile.posX][tile.posY - 1].player == currentPlayer || (handler.tiles[tile.posX][tile.posY - 1].player != currentPlayer && handler.tiles[tile.posX][tile.posY - 1].invaded)) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
    
                    try
                    {
                        if (handler.tiles[tile.posX][tile.posY + 1].player == currentPlayer || (handler.tiles[tile.posX][tile.posY + 1].player != currentPlayer && handler.tiles[tile.posX][tile.posY + 1].invaded)) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
    
                    try
                    {
                        if (handler.tiles[tile.posX + 1][tile.posY - 1].player == currentPlayer || (handler.tiles[tile.posX + 1][tile.posY - 1].player != currentPlayer && handler.tiles[tile.posX + 1][tile.posY - 1].invaded)) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
    
                    try
                    {
                        if (handler.tiles[tile.posX + 1][tile.posY].player == currentPlayer || (handler.tiles[tile.posX + 1][tile.posY].player != currentPlayer && handler.tiles[tile.posX + 1][tile.posY].invaded)) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
    
                    try
                    {
                        if (handler.tiles[tile.posX + 1][tile.posY + 1].player == currentPlayer || (handler.tiles[tile.posX + 1][tile.posY + 1].player != currentPlayer && handler.tiles[tile.posX + 1][tile.posY + 1].invaded)) tile.highlighted = true;
                    }
                    catch (Exception ignored) {}
                }
            }
        }
        
        if (invaded[0][0] && invaded[0][1] && invaded[0][2] && invaded[0][3])
        {
            win.winner = 1;
            game.gameState = State.Win;
            handler.tiles = new Tile[0][0];
            Game.updateDiscord("In menu", "Game ending");
        }
        else if (invaded[1][0] && invaded[1][1] && invaded[1][2] && invaded[1][3])
        {
            win.winner = 0;
            game.gameState = State.Win;
            handler.tiles = new Tile[0][0];
            Game.updateDiscord("In menu", "Game ending");
        }
    }

    public void tick()
    {

    }

    public void render(Graphics g)
    {
        switch (settings.settings.get("theme"))
        {
            case "light" : g.setColor(new Color(190, 190, 190)); break;
            case "dark"  : g.setColor(new Color(50, 50, 50));    break;
        }

        g.fillRect(0, Game.HEIGHT - 150 - 5, Game.WIDTH, 5);

        switch (settings.settings.get("theme"))
        {
            case "light" : g.setColor(new Color(230, 230, 230)); break;
            case "dark"  : g.setColor(new Color(60, 60, 60));    break;
        }

        g.fillRect(0, Game.HEIGHT - 150, Game.WIDTH, 150);

        switch (settings.settings.get("theme"))
        {
            case "light" : g.setColor(Color.BLACK); break;
            case "dark"  : g.setColor(Color.WHITE); break;
        }

        g.setFont(Game.font.deriveFont(30f));
        g.drawString(Game.BUNDLE.getString("hud.current_player"), 20, Game.HEIGHT - 150 + 20 + 25);

        g.setFont(Game.font.deriveFont(20f));
        g.drawString(customise.playerName[currentPlayer - 1], 20, Game.HEIGHT - 20);

        g.setColor(customise.playerColor[currentPlayer - 1]);
        g.fillRect(20, Game.HEIGHT - 20 - 25 - 30, 30, 30);

        if (Game.PAUSED)
        {
            switch (settings.settings.get("theme"))
            {
                case "light" : g.setColor(new Color(1, 1, 1, .6f)); break;
                case "dark"  : g.setColor(new Color(0, 0, 0, .3f)); break;
            }

            g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

            switch (settings.settings.get("theme"))
            {
                case "light" : g.setColor(Color.BLACK); break;
                case "dark"  : g.setColor(Color.WHITE); break;
            }

            g.setFont(Game.font.deriveFont(70f));
            g.drawString(Game.BUNDLE.getString("hud.paused").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(70f)).stringWidth(Game.BUNDLE.getString("hud.paused").toUpperCase()) / 2, Game.HEIGHT / 2 - 150);

            g.setFont(Game.font.deriveFont(30f));
            g.drawString(Game.BUNDLE.getString("hud.continue"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("hud.continue")) / 2, Game.HEIGHT / 2 - 90);
            g.drawString(Game.BUNDLE.getString("hud.menu"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("hud.menu")) / 2, Game.HEIGHT / 2 - 60);
            g.drawString(Game.BUNDLE.getString("hud.quit"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("hud.quit")) / 2, Game.HEIGHT / 2 - 30);
        }
    }
}