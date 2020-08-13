package com.xwars.main.input;

import com.xwars.gameobjects.Tile;
import com.xwars.main.AudioPlayer;
import com.xwars.main.Game;
import com.xwars.main.Handler;
import com.xwars.main.State;
import com.xwars.states.Customise;
import com.xwars.states.HUD;
import com.xwars.states.Rules;
import com.xwars.states.Settings;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Random;

/**
 * Used to monitor mouse input to the application
 *
 * @author soni801
 */

public class MouseInput extends MouseAdapter
{
    private final Handler handler;
    private final HUD hud;
    private final Game game;
    private final Customise customise;
    private final Settings settings;
    private final Rules rules;

    public int panX, panY;
    public int dragX, dragY;

    int startX, startY;

    Graphics g;
    
    private Tile lastTile = null;
    private boolean movingWindow;

    public MouseInput(Handler handler, HUD hud, Game game, Customise customise, Settings settings, Rules rules)
    {
        this.handler = handler;
        this.hud = hud;
        this.game = game;
        this.customise = customise;
        this.settings = settings;
        this.rules = rules;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        int mx = e.getX();
        int my = e.getY();

        startX = mx;
        startY = my;

        if (mouseOver(mx, my, Game.WIDTH - 10 - 1 - 15, 10, 15, 15, false)) System.exit(1);
        if (mouseOver(mx, my, Game.WIDTH - 10 - 1 - 15 - 10 - 15, 10, 15, 15, false)) game.window.frame.setState(Frame.ICONIFIED);
    
        if (mouseOver(mx, my, 0, 0, Game.WIDTH, 36, false)) movingWindow = true;
        
        switch (game.gameState)
        {
            case Menu :
                // Play button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220 - 60 - 30, 200, 40, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    game.gameState = State.Customise;
                    Game.updateDiscord("In menu", "Starting game");
                }
                // Play button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220 - 30, 200, 40, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    game.gameState = State.Rules;
                }
                // Settings button
                else if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220 + 60 - 30, 200, 40, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    game.gameState = State.Settings;
                }
                // Quit button
                else if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220  + 120 - 30, 200, 40, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    System.exit(1);
                }
                break;
            case Customise :
                // Play online/offline button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT - 50 - 10 - 50 - 10 - 50 - 10, 240, 50, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    customise.online = !customise.online;
                    customise.changingName = 0;
                    customise.colorPicker = 0;
                }
                // Start/Connect button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT - 50 - 10 - 50 - 10, 240, 50, false))
                {
                    if (customise.online)
                    {
                        if (customise.onlineMode == 0)
                        {
                            if (!game.client.connectionActive)
                            {
                                AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                                game.client.start();
                                game.client.connect(customise.ip);
                            }
                        }
                        else
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                            game.startGame(0, 0);
                        }
                    }
                    else
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        game.startGame(0, 0);
                        Game.updateDiscord("In game", "Playing locally");
                    }
                }
                // Back button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT - 50 - 10, 240, 50, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    game.gameState = State.Menu;
                    customise.changingName = 0;
                    customise.colorPicker = 0;
                    Game.updateDiscord("In menu", "Main menu");
                }
                if (customise.online)
                {
                    // Join Game
                    if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT / 2, 240, 50, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        customise.onlineMode = 0;
                    }
                    // Host Game
                    if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT / 2 - 50 - 10, 240, 50, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        customise.onlineMode = 1;
                    }
                }
                // Color picker 1
                if (mouseOver(mx, my, 10 + 30 + 10, Game.HEIGHT - 10 - 60, 30, 30, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    if (customise.colorPicker != 1)
                    {
                        customise.r = customise.playerColor[0].getRed();
                        customise.g = customise.playerColor[0].getGreen();
                        customise.b = customise.playerColor[0].getBlue();

                        customise.colorPicker = 1;
                    }
                    else customise.colorPicker = 0;
                }
                // Random color 1
                if (mouseOver(mx, my, 10 + 30 + 10 + 30 + 10, Game.HEIGHT - 10 - 60, 30, 30, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    Random r = new Random();
                    customise.playerColor[0] = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
                }
                // Player name 1
                if (mouseOver(mx, my, 10 + g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(customise.playerName[0]) + 10, Game.HEIGHT - 10 - 25, 30, 30, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    customise.playerName[0] = "";
                    customise.changingName = 1;
                    customise.typing = false;
                }
                if (customise.online)
                {
                    // Change IP
                    if (mouseOver(mx, my, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(customise.ip) - 10 - 30, Game.HEIGHT - 10 - 25, 30, 30, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        customise.ip = "";
                        customise.typing = true;
                        customise.changingName = 0;
                    }
                }
                else
                {
                    // Color picker 2
                    if (mouseOver(mx, my, Game.WIDTH - 10 - 30 - 10 - 30, Game.HEIGHT - 10 - 60, 30, 30, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        if (customise.colorPicker != 2)
                        {
                            customise.r = customise.playerColor[1].getRed();
                            customise.g = customise.playerColor[1].getGreen();
                            customise.b = customise.playerColor[1].getBlue();

                            customise.colorPicker = 2;
                        }
                        else customise.colorPicker = 0;
                    }
                    // Random color 2
                    if (mouseOver(mx, my, Game.WIDTH - 15 - 30 - 10 - 30 - 10 - 30, Game.HEIGHT - 10 - 60, 30, 30, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        Random r = new Random();
                        customise.playerColor[1] = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
                    }
                    // Player name 2
                    if (mouseOver(mx, my, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(customise.playerName[1]) - 10 - 30, Game.HEIGHT - 10 - 25, 30, 30, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        customise.playerName[1] = "";
                        customise.changingName = 2;
                    }
                }
                // Board size 1 +
                if (mouseOver(mx, my, Game.WIDTH / 2 - 10 - 5 - 100, 120, 20, 20, false))
                {
                    if (!(customise.boardSize[0] >= 100))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        customise.boardSize[0]++;
                    }
                }
                // Board size 1 -
                if (mouseOver(mx, my, Game.WIDTH / 2 - 10 - 5 - 100, 120 + 20, 20, 20, false))
                {
                    if (!(customise.boardSize[0] <= 10))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        customise.boardSize[0]--;
                    }
                }
                // Board size 2 +
                if (mouseOver(mx, my, Game.WIDTH / 2 + 10 + 100 - 20 + 1, 120, 20, 20, false))
                {
                    if (!(customise.boardSize[1] >= 100))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        customise.boardSize[1]++;
                    }
                }
                // Board size 2 -
                if (mouseOver(mx, my, Game.WIDTH / 2 + 10 + 100 - 20 + 1, 120 + 20, 20, 20, false))
                {
                    if (!(customise.boardSize[1] <= 10))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        customise.boardSize[1]--;
                    }
                }
                break;
            case Settings :
                switch (settings.page)
                {
                    case 1:
                        // Theme Previous
                        if (mouseOver(mx, my, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                            if (settings.settings.get("theme").equals("dark")) settings.settings.replace("theme", "light");
                        }
                        // Theme Next
                        if (mouseOver(mx, my, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                            if (settings.settings.get("theme").equals("light")) settings.settings.replace("theme", "dark");
                        }
                        // Resolution Previous
                        if (mouseOver(mx, my, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 + 80 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                            if (settings.settings.get("resolution").equals("fullscreen"))
                            {
                                settings.settings.replace("resolution", "1600x900");
                                break;
                            }
                            else if (settings.settings.get("resolution").equals("1600x900"))
                            {
                                settings.settings.replace("resolution", "1280x720");
                                break;
                            }
                            else if (settings.settings.get("resolution").equals("1280x720"))
                            {
                                settings.settings.replace("resolution", "960x540");
                                break;
                            }
                        }
                        // Resolution Next
                        if (mouseOver(mx, my, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70  + 80 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                            if (settings.settings.get("resolution").equals("960x540"))
                            {
                                settings.settings.replace("resolution", "1280x720");
                                break;
                            }
                            else if (settings.settings.get("resolution").equals("1280x720"))
                            {
                                settings.settings.replace("resolution", "1600x900");
                                break;
                            }
                            else if (settings.settings.get("resolution").equals("1600x900"))
                            {
                                settings.settings.replace("resolution", "fullscreen");
                                break;
                            }
                        }
                        // Show FPS Previous
                        if (mouseOver(mx, my, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 + 160 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                            if (settings.settings.get("showfps").equals("true")) settings.settings.replace("showfps", "false");
                        }
                        // Show FPS Next
                        if (mouseOver(mx, my, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70  + 160 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                            if (settings.settings.get("showfps").equals("false")) settings.settings.replace("showfps", "true");
                        }
                        break;
                }
                // Reset Settings
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.reset").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 50 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.reset").toUpperCase()), 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    settings.reset();
                }
                // Back
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()), 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    game.gameState = State.Menu;
                }
                // Previous Page
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<") / 2 - 100, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<"), 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    if (settings.page > 1) settings.page--;
                }
                // Next Page
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<") / 2 + 100, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<"), 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    if (settings.page < 2) settings.page++;
                }
                break;
            case Rules:
                // Back
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("rules.back").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()), 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    game.gameState = State.Menu;
                }
                break;
            case Game :
                if (Game.PAUSED)
                {
                    // Continue button
                    if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Continue") / 2, Game.HEIGHT / 2 - 90 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Continue"), 20, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        Game.PAUSED = false;
                    }
                    // Menu button
                    if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Menu") / 2, Game.HEIGHT / 2 - 60 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Menu"), 20, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        handler.tiles = new Tile[0][0];
                        Game.PAUSED = false;
                        game.gameState = State.Menu;
                        Game.updateDiscord("In menu", "Main menu");
                    }
                    // Quit button
                    if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Exit") / 2, Game.HEIGHT / 2 - 30 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Exit"), 20, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                        System.exit(1);
                    }
                }
                break;
            case Win:
                // Back
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("win.menu").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()), 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(settings.settings.get("volume")));
                    game.gameState = State.Menu;
                }
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        int mx = e.getX();
        int my = e.getY();
        
        movingWindow = false;

        if (game.gameState == State.Game)
        {
            if (!Game.PAUSED)
            {
                if (mx == startX && my == startY)
                {
                    if (hud.active)
                    {
                        if (!customise.online || hud.currentPlayer == 1)
                        {
                            try
                            {
                                Tile tile = handler.tiles[(mx + dragX) / 25][(my + dragY) / 25];
            
                                if (tile.highlighted)
                                {
                                    if (tile.player == 0)
                                    {
                                        tile.player = hud.currentPlayer;
                                        System.out.println("Player " + hud.currentPlayer + " (" + customise.playerName[hud.currentPlayer - 1] + ") has taken tile " + tile.posX + ", " + tile.posY);
        
                                        hud.changePlayer();
        
                                        if (customise.online)
                                        {
                                            String x = String.valueOf(tile.posX), y = String.valueOf(tile.posY);
                                            while (x.length() < 3) x = "0" + x;
                                            while (y.length() < 3) y = "0" + y;
            
                                            switch (customise.onlineMode)
                                            {
                                                case 0 : game.client.send("t" + x + y); break;
                                                case 1 : game.server.send("t" + x + y); break;
                                            }
                                        }
                                    }
                                    else if (tile.foundation != 0 && tile.player != hud.currentPlayer)
                                    {
                                        tile.invaded = true;
                                        System.out.println("Player " + hud.currentPlayer + " (" + customise.playerName[hud.currentPlayer - 1] + ") invaded foundation at " + tile.posX + ", " + tile.posY);
    
                                        hud.changePlayer();
    
                                        if (customise.online)
                                        {
                                            String x = String.valueOf(tile.posX), y = String.valueOf(tile.posY);
                                            while (x.length() < 3) x = "0" + x;
                                            while (y.length() < 3) y = "0" + y;
        
                                            switch (customise.onlineMode)
                                            {
                                                case 0 : game.client.send("t" + x + y); break;
                                                case 1 : game.server.send("t" + x + y); break;
                                            }
                                        }
                                    }
                                }
                                
                                
                            }
                            catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {}
                        }
                    }
                    else hud.initialise();
                }
                else
                {
                    panX = dragX;
                    panY = dragY;
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        int mx = e.getX();
        int my = e.getY();

        if (mouseOver(mx, my, 0, 0, Game.WIDTH, 36, false) && movingWindow)
        {
            if (!settings.settings.get("resolution").equals("fullscreen")) game.window.frame.setLocation(game.window.frame.getX() - (startX - mx), game.window.frame.getY() - (startY - my));
        }
        else
        {
            switch (game.gameState)
            {
                case Customise :
                    switch (customise.colorPicker)
                    {
                        case 1 :
                            if (mouseOver(mx, my, 10 + 30 + customise.r - 5, Game.HEIGHT - 100 - 10 - 120 + 30 - 10, 10, 20, false))
                            {
                                customise.r = mx - (10 + 30);
                                if (customise.r > 255) customise.r = 255;
                                if (customise.r < 0) customise.r = 0;
                            }
                            if (mouseOver(mx, my, 10 + 30 + customise.g - 5, Game.HEIGHT - 100 - 10 - 120 + 60 - 10, 10, 20, false))
                            {
                                customise.g = mx - (10 + 30);
                                if (customise.g > 255) customise.g = 255;
                                if (customise.g < 0) customise.g = 0;
                            }
                            if (mouseOver(mx, my, 10 + 30 + customise.b - 5, Game.HEIGHT - 100 - 10 - 120 + 90 - 10, 10, 20, false))
                            {
                                customise.b = mx - (10 + 30);
                                if (customise.b > 255) customise.b = 255;
                                if (customise.b < 0) customise.b = 0;
                            }
                            break;
                        case 2 :
                            if (mouseOver(mx, my, Game.WIDTH - 15 - (30 + 255 + 30) + 30 + customise.r - 5, Game.HEIGHT - 100 - 10 - 120 + 30 - 10, 10, 20, false))
                            {
                                customise.r = mx - (Game.WIDTH - 15 - (30 + 255 + 30) + 30);
                                if (customise.r > 255) customise.r = 255;
                                if (customise.r < 0) customise.r = 0;
                            }
                            if (mouseOver(mx, my, Game.WIDTH - 15 - (30 + 255 + 30) + 30 + customise.g - 5, Game.HEIGHT - 100 - 10 - 120 + 60 - 10, 10, 20, false))
                            {
                                customise.g = mx - (Game.WIDTH - 15 - (30 + 255 + 30) + 30);
                                if (customise.g > 255) customise.g = 255;
                                if (customise.g < 0) customise.g = 0;
                            }
                            if (mouseOver(mx, my, Game.WIDTH - 15 - (30 + 255 + 30) + 30 + customise.b - 5, Game.HEIGHT - 100 - 10 - 120 + 90 - 10, 10, 20, false))
                            {
                                customise.b = mx - (Game.WIDTH - 15 - (30 + 255 + 30) + 30);
                                if (customise.b > 255) customise.b = 255;
                                if (customise.b < 0) customise.b = 0;
                            }
                            break;
                    }
                    break;
                case Settings:
                    if (settings.page == 2)
                    {
                        // Volume
                        if (mouseOver(mx, my, Game.WIDTH / 2 - 200, Game.HEIGHT / 2 - 70 + 30 - 10, 400, 20, false))
                        {
                            settings.settings.replace("volume", String.valueOf((mx - (float)(Game.WIDTH / 2 - 200)) / 400));
                        }
                    }
                    break;
                case Game :
                    if (!Game.PAUSED)
                    {
                        dragX = panX + (startX - mx);
                        dragY = panY + (startY - my);
                    }
                    break;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        int mx = e.getX();
        int my = e.getY();

        if (mouseOver(mx, my, Game.WIDTH - 10 - 1 - 15, 10, 15, 15, false)) game.selected_close_operation = 1;
        else if (mouseOver(mx, my, Game.WIDTH - 10 - 1 - 15 - 10 - 15, 10, 15, 15, false)) game.selected_close_operation = 2;
        else game.selected_close_operation = 0;
    
        if (game.gameState == State.Game)
        {
            if (!Game.PAUSED)
            {
                if (!customise.online || hud.currentPlayer == 1)
                {
                    try
                    {
                        Tile tile = handler.tiles[(mx + dragX) / 25][(my + dragY) / 25];
    
                        if (lastTile != null) lastTile.hover = 0;
    
                        if (tile.highlighted)
                        {
                            if (tile.player == 0 || (tile.foundation != 0 && tile.player != hud.currentPlayer))
                            {
                                tile.hover = hud.currentPlayer;
                            }
                        }
    
                        lastTile = tile;
                    }
                    catch (ArrayIndexOutOfBoundsException | NullPointerException exception)
                    {
                        if (lastTile != null) lastTile.hover = 0;
                    }
                }
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (game.gameState == State.Rules)
        {
            rules.offset += e.getWheelRotation() * 10;
        }
    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height, boolean movable)
    {
        if (movable) return (((mx + dragX > x) && (mx + dragX < x + width)) && ((my + dragY > y) && (my + dragY < y + height)));
        return (((mx > x) && (mx < x + width)) && ((my > y) && (my < y + height)));
    }

    public void render(Graphics g)
    {
        this.g = g;
    }
}