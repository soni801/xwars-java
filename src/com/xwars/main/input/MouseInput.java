package com.xwars.main.input;

import com.xwars.enums.CloseOperation;
import com.xwars.enums.MessageMode;
import com.xwars.enums.OnlineMode;
import com.xwars.gameobjects.Tile;
import com.xwars.main.AudioPlayer;
import com.xwars.main.Game;
import com.xwars.main.Handler;
import com.xwars.enums.State;
import com.xwars.main.loaders.ResourceLoader;
import com.xwars.online.Message;
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
 * Monitors application mouse input
 *
 * @author Soni
 * @version 1.1
 */
public class MouseInput extends MouseAdapter
{
    private final ResourceLoader resourceLoader;
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
    
    /**
     * Constructor. Create only one instance of this object
     *
     * @param resourceLoader Instance of the ResourceLoader class
     * @param handler The Handler instance used in the application
     * @param hud Instance of the HUD class
     * @param game The current Game object
     * @param customise Instance of the Customise class
     * @param settings Instance of the Settings class
     * @param rules Instance of the Rules class
     */
    public MouseInput(ResourceLoader resourceLoader, Handler handler, HUD hud, Game game, Customise customise, Settings settings, Rules rules)
    {
        this.resourceLoader = resourceLoader;
        this.handler = handler;
        this.hud = hud;
        this.game = game;
        this.customise = customise;
        this.settings = settings;
        this.rules = rules;
    }
    
    /**
     * Executed when mouse is pressed inside the application
     *
     * @param e The registered MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        int mx = e.getX();
        int my = e.getY();

        startX = mx;
        startY = my;

        if (mouseOver(mx, my, Game.WIDTH - 10 - 1 - 15, 10, 15, 15)) System.exit(1);
        if (mouseOver(mx, my, Game.WIDTH - 10 - 1 - 15 - 10 - 15, 10, 15, 15)) game.window.frame.setState(Frame.ICONIFIED);
    
        if (mouseOver(mx, my, 0, 0, Game.WIDTH, 36)) movingWindow = true;
        
        switch (game.gameState)
        {
            case Menu :
                // Play button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220 - 60 - 30, 200, 40))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    game.gameState = State.Customise;
                    Game.updateDiscord("In menu", "Starting game");
                }
                // Play button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220 - 30, 200, 40))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    game.gameState = State.Rules;
                }
                // Settings button
                else if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220 + 60 - 30, 200, 40))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    game.gameState = State.Settings;
                }
                // Quit button
                else if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220  + 120 - 30, 200, 40))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    System.exit(1);
                }
                break;
            case Customise :
                // Play online/offline button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT - 50 - 10 - 50 - 10 - 50 - 10, 240, 50))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    customise.online = !customise.online;
                    customise.changingName = 0;
                    customise.colorPicker = 0;
                }
                // Start/Connect button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT - 50 - 10 - 50 - 10, 240, 50))
                {
                    if (customise.online)
                    {
                        if (customise.onlineMode == OnlineMode.Client)
                        {
                            if (!game.client.connectionActive)
                            {
                                AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                                game.client.start();
                                game.client.connect(customise.ip);
                            }
                        }
                        else
                        {
                            AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                            game.startGame(0, 0);
                        }
                    }
                    else
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        game.startGame(0, 0);
                        Game.updateDiscord("In game", "Playing locally");
                    }
                }
                // Back button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT - 50 - 10, 240, 50))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    game.gameState = State.Menu;
                    customise.changingName = 0;
                    customise.colorPicker = 0;
                    Game.updateDiscord("In menu", "Main menu");
                }
                if (customise.online)
                {
                    // Join Game
                    if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT / 2, 240, 50))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        customise.onlineMode = OnlineMode.Client;
                    }
                    // Host Game
                    if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT / 2 - 50 - 10, 240, 50))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        customise.onlineMode = OnlineMode.Server;
                    }
                }
                // Color picker 1
                if (mouseOver(mx, my, 10 + 30 + 10, Game.HEIGHT - 10 - 60, 30, 30))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
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
                if (mouseOver(mx, my, 10 + 30 + 10 + 30 + 10, Game.HEIGHT - 10 - 60, 30, 30))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    Random r = new Random();
                    customise.playerColor[0] = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
                }
                // Player name 1
                if (mouseOver(mx, my, 10 + g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(customise.playerName[0]) + 10, Game.HEIGHT - 10 - 25, 30, 30))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    customise.playerName[0] = "";
                    customise.changingName = 1;
                    customise.typing = false;
                }
                if (customise.online)
                {
                    // Change IP
                    if (mouseOver(mx, my, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(customise.ip) - 10 - 30, Game.HEIGHT - 10 - 25, 30, 30))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        customise.ip = "";
                        customise.typing = true;
                        customise.changingName = 0;
                    }
                }
                else
                {
                    // Color picker 2
                    if (mouseOver(mx, my, Game.WIDTH - 10 - 30 - 10 - 30, Game.HEIGHT - 10 - 60, 30, 30))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
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
                    if (mouseOver(mx, my, Game.WIDTH - 15 - 30 - 10 - 30 - 10 - 30, Game.HEIGHT - 10 - 60, 30, 30))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        Random r = new Random();
                        customise.playerColor[1] = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
                    }
                    // Player name 2
                    if (mouseOver(mx, my, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(customise.playerName[1]) - 10 - 30, Game.HEIGHT - 10 - 25, 30, 30))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        customise.playerName[1] = "";
                        customise.changingName = 2;
                    }
                }
                // Board size 1 +
                if (mouseOver(mx, my, Game.WIDTH / 2 - 10 - 5 - 100, 120, 20, 20))
                {
                    if (!(customise.boardSize[0] >= 100))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        customise.boardSize[0]++;
                    }
                }
                // Board size 1 -
                if (mouseOver(mx, my, Game.WIDTH / 2 - 10 - 5 - 100, 120 + 20, 20, 20))
                {
                    if (!(customise.boardSize[0] <= 10))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        customise.boardSize[0]--;
                    }
                }
                // Board size 2 +
                if (mouseOver(mx, my, Game.WIDTH / 2 + 10 + 100 - 20 + 1, 120, 20, 20))
                {
                    if (!(customise.boardSize[1] >= 100))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        customise.boardSize[1]++;
                    }
                }
                // Board size 2 -
                if (mouseOver(mx, my, Game.WIDTH / 2 + 10 + 100 - 20 + 1, 120 + 20, 20, 20))
                {
                    if (!(customise.boardSize[1] <= 10))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        customise.boardSize[1]--;
                    }
                }
                break;
            case Settings :
                if (settings.page == 1)
                {// Theme Previous
                    if (mouseOver(mx, my, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 - 13, 40, 40))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        if (settings.get("theme").equals("dark")) settings.update("theme", 0);
                    }
                    // Theme Next
                    if (mouseOver(mx, my, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70 - 13, 40, 40))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        if (settings.get("theme").equals("light")) settings.update("theme", 1);
                    }
                    // Resolution Previous
                    if (mouseOver(mx, my, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 + 80 - 13, 40, 40))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        switch (settings.get("resolution"))
                        {
                            case "fullscreen" -> settings.update("resolution", 2);
                            case "900" -> settings.update("resolution", 1);
                            case "720" -> settings.update("resolution", 0);
                        }
                    }
                    // Resolution Next
                    if (mouseOver(mx, my, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70 + 80 - 13, 40, 40))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        switch (settings.get("resolution"))
                        {
                            case "540" -> settings.update("resolution", 1);
                            case "720" -> settings.update("resolution", 2);
                            case "900" -> settings.update("resolution", 3);
                        }
                    }
                    // Show FPS Previous
                    if (mouseOver(mx, my, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 + 160 - 13, 40, 40))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        if (settings.get("displayFPS").equals("true")) settings.update("displayFPS", 1);
                    }
                    // Show FPS Next
                    if (mouseOver(mx, my, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70 + 160 - 13, 40, 40))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        if (settings.get("displayFPS").equals("false")) settings.update("displayFPS", 0);
                    }
                }
                // Reset Settings
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.reset").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 50 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.reset").toUpperCase()), 20))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    settings.reset();
                }
                // Back
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()), 20))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    game.gameState = State.Menu;
                }
                // Previous Page
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<") / 2 - 100, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<"), 20))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    if (settings.page > 1) settings.page--;
                }
                // Next Page
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<") / 2 + 100, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<"), 20))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    if (settings.page < 2) settings.page++;
                }
                break;
            case Rules:
                // Back
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("rules.back").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()), 20))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    game.gameState = State.Menu;
                }
                break;
            case Game :
                if (Game.PAUSED)
                {
                    // Continue button
                    if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Continue") / 2, Game.HEIGHT / 2 - 90 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Continue"), 20))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        Game.PAUSED = false;
                    }
                    // Menu button
                    if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Menu") / 2, Game.HEIGHT / 2 - 60 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Menu"), 20))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        handler.tiles = new Tile[0][0];
                        Game.PAUSED = false;
                        game.gameState = State.Menu;
                        Game.updateDiscord("In menu", "Main menu");
                    }
                    // Quit button
                    if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Exit") / 2, Game.HEIGHT / 2 - 30 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Exit"), 20))
                    {
                        AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                        System.exit(1);
                    }
                }
                break;
            case Win:
                // Back
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("win.menu").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()), 20))
                {
                    AudioPlayer.playAudio(resourceLoader.getAudioClip("/audio/click.au"), Float.parseFloat(settings.get("volume")));
                    game.gameState = State.Menu;
                }
                break;
        }
    }
    
    /**
     * Executed when mouse is released inside the application
     *
     * @param e The registered MouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        int mx = e.getX();
        int my = e.getY();
        
        movingWindow = false;

        // Check if gameState is Game
        if (game.gameState == State.Game)
        {
            // Check that game is not paused
            if (!Game.PAUSED)
            {
                // Check if playing area is not moved
                if (mx == startX && my == startY)
                {
                    // Check if game has loaded and is ready to be clicked
                    if (hud.active)
                    {
                        // Check if your player is able to place tile
                        if (!customise.online || hud.currentPlayer == 1)
                        {
                            try
                            {
                                Tile tile = handler.tiles[(mx + dragX) / 25][(my + dragY) / 25];
                                
                                // Check that you are able to place tile
                                if (tile.highlighted)
                                {
                                    // Check that tile is not already owned
                                    if (tile.player == 0)
                                    {
                                        tile.player = hud.currentPlayer;
                                        System.out.println("Player " + hud.currentPlayer + " (" + customise.playerName[hud.currentPlayer - 1] + ") has taken tile " + tile.posX + ", " + tile.posY);
        
                                        hud.changePlayer();
        
                                        // Check if online play is active to send data
                                        if (customise.online)
                                        {
                                            // Create Message object
                                            Message message = new Message();
                                            message.mode = MessageMode.Tile;
                                            message.position[0] = tile.posX;
                                            message.position[1] = tile.posY;
                                            
                                            switch (customise.onlineMode)
                                            {
                                                case Client : game.client.send(message); break;
                                                case Server : game.server.send(message); break;
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
                                            Message message = new Message();
                                            message.mode = MessageMode.Tile;
                                            message.position = new int[]{tile.posX, tile.posY};
                                            message.invade = true;
    
                                            switch (customise.onlineMode)
                                            {
                                                case Client : game.client.send(message); break;
                                                case Server : game.server.send(message); break;
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
    
    /**
     * Executed when mouse is dragged inside application
     *
     * @param e The registered MouseEvent
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        int mx = e.getX();
        int my = e.getY();

        if (mouseOver(mx, my, 0, 0, Game.WIDTH, 36) && movingWindow)
        {
            if (!settings.get("resolution").equals("fullscreen")) game.window.frame.setLocation(game.window.frame.getX() - (startX - mx), game.window.frame.getY() - (startY - my));
        }
        else
        {
            switch (game.gameState)
            {
                case Customise :
                    switch (customise.colorPicker)
                    {
                        case 1 :
                            if (mouseOver(mx, my, 10 + 30 + customise.r - 5, Game.HEIGHT - 100 - 10 - 120 + 30 - 10, 10, 20))
                            {
                                customise.r = mx - (10 + 30);
                                if (customise.r > 255) customise.r = 255;
                                if (customise.r < 0) customise.r = 0;
                            }
                            if (mouseOver(mx, my, 10 + 30 + customise.g - 5, Game.HEIGHT - 100 - 10 - 120 + 60 - 10, 10, 20))
                            {
                                customise.g = mx - (10 + 30);
                                if (customise.g > 255) customise.g = 255;
                                if (customise.g < 0) customise.g = 0;
                            }
                            if (mouseOver(mx, my, 10 + 30 + customise.b - 5, Game.HEIGHT - 100 - 10 - 120 + 90 - 10, 10, 20))
                            {
                                customise.b = mx - (10 + 30);
                                if (customise.b > 255) customise.b = 255;
                                if (customise.b < 0) customise.b = 0;
                            }
                            break;
                        case 2 :
                            if (mouseOver(mx, my, Game.WIDTH - 15 - (30 + 255 + 30) + 30 + customise.r - 5, Game.HEIGHT - 100 - 10 - 120 + 30 - 10, 10, 20))
                            {
                                customise.r = mx - (Game.WIDTH - 15 - (30 + 255 + 30) + 30);
                                if (customise.r > 255) customise.r = 255;
                                if (customise.r < 0) customise.r = 0;
                            }
                            if (mouseOver(mx, my, Game.WIDTH - 15 - (30 + 255 + 30) + 30 + customise.g - 5, Game.HEIGHT - 100 - 10 - 120 + 60 - 10, 10, 20))
                            {
                                customise.g = mx - (Game.WIDTH - 15 - (30 + 255 + 30) + 30);
                                if (customise.g > 255) customise.g = 255;
                                if (customise.g < 0) customise.g = 0;
                            }
                            if (mouseOver(mx, my, Game.WIDTH - 15 - (30 + 255 + 30) + 30 + customise.b - 5, Game.HEIGHT - 100 - 10 - 120 + 90 - 10, 10, 20))
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
                        if (mouseOver(mx, my, Game.WIDTH / 2 - 200, Game.HEIGHT / 2 - 70 + 30 - 10, 400, 20))
                        {
                            settings.update("volume", (mx + 20 - (Game.WIDTH / 2 - 200)) / 40);
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
    
    /**
     * Executed on mouse movement inside the application
     *
     * @param e The registered MouseEvent
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        int mx = e.getX();
        int my = e.getY();

        if (mouseOver(mx, my, Game.WIDTH - 10 - 1 - 15, 10, 15, 15)) game.selected_close_operation = CloseOperation.Close;
        else if (mouseOver(mx, my, Game.WIDTH - 10 - 1 - 15 - 10 - 15, 10, 15, 15)) game.selected_close_operation = CloseOperation.Minimise;
        else game.selected_close_operation = CloseOperation.None;
    
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
    
    /**
     * Executed upon mouse wheel scroll inside the application
     *
     * @param e The registered MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (game.gameState == State.Rules)
        {
            rules.offset += e.getWheelRotation() * 10;
        }
    }
    
    /**
     * Calculates if mouse cursor is inside the specified area
     *
     * @param mx Horizontal cursor location
     * @param my Vertical cursor location
     * @param x Horizontal position of area
     * @param y Vertical position of area
     * @param width Width of area
     * @param height Height of area
     * @return Whether the cursor is inside the specified area
     */
    private boolean mouseOver(int mx, int my, int x, int y, int width, int height)
    {
        return (((mx > x) && (mx < x + width)) && ((my > y) && (my < y + height)));
    }
    
    /**
     * Sets g to an instance of the Graphics class
     *
     * @param g Instance of the Graphics class
     */
    public void render(Graphics g)
    {
        this.g = g;
    }
}