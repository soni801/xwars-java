package com.xwars.main.input;

import com.xwars.gameobjects.Tile;
import com.xwars.main.*;
import com.xwars.states.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    public int panX, panY;
    public int dragX, dragY;

    int startX, startY;

    Graphics g;

    public MouseInput(Handler handler, HUD hud, Game game, Customise customise, Settings settings)
    {
        this.handler = handler;
        this.hud = hud;
        this.game = game;
        this.customise = customise;
        this.settings = settings;
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

        switch (game.gameState)
        {
            case Menu :
                // Play button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220 - 30, 200, 40, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    game.gameState = State.Customise;
                    Game.updateDiscord("In menu", "Starting game");
                }
                // Settings button
                else if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220 + 60 - 30, 200, 40, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    game.gameState = State.Settings;
                }
                // Quit button
                else if (mouseOver(mx, my, Game.WIDTH / 2 - 2 - 100, Game.HEIGHT - 220  + 120 - 30, 200, 40, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    System.exit(1);
                }
                break;
            case Customise :
                // Play online/offline button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT - 50 - 10 - 50 - 10 - 50 - 10, 240, 50, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    customise.online = !customise.online;
                    customise.changingName = 0;
                    customise.colorPicker = 0;
                }
                // Start/Connect button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT - 50 - 10 - 50 - 10, 240, 50, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    if (customise.online)
                    {
                        if (customise.onlineMode == 0)
                        {
                            if (!game.client.connectionActive)
                            {
                                game.client.start();
                                game.client.connect(customise.ip);
                            }
                        }
                        else
                        {
                            try
                            {
                                if (game.server.connectionActive)
                                {
                                    String nameLength = String.valueOf(customise.playerName[0].length());
                                    while (nameLength.length() < 2) nameLength = "0" + nameLength;

                                    String name = customise.playerName[0];

                                    String r = String.valueOf(customise.playerColor[0].getRed()), g = String.valueOf(customise.playerColor[0].getGreen()), b = String.valueOf(customise.playerColor[0].getBlue());
                                    while (r.length() < 3) r = "0" + r;
                                    while (g.length() < 3) g = "0" + g;
                                    while (b.length() < 3) b = "0" + b;

                                    game.startGame();
                                    game.server.sendUTF("s" + nameLength + name + r + g + b);
                                }
                            }
                            catch (Exception ignored) {}
                        }
                    }
                    else
                    {
                        game.startGame();
                        Game.updateDiscord("In game", "Playing locally");
                    }
                }
                // Back button
                if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT - 50 - 10, 240, 50, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
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
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                        customise.onlineMode = 0;
                    }
                    // Host Game
                    if (mouseOver(mx, my, Game.WIDTH / 2 - 120, Game.HEIGHT / 2 - 50 - 10, 240, 50, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                        customise.onlineMode = 1;
                    }
                }
                // Color picker 1
                if (mouseOver(mx, my, 10 + 30 + 10, Game.HEIGHT - 10 - 60, 30, 30, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
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
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    Random r = new Random();
                    customise.playerColor[0] = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
                }
                // Player name 1
                if (mouseOver(mx, my, 10 + g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(customise.playerName[0]) + 10, Game.HEIGHT - 10 - 25, 30, 30, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    customise.playerName[0] = "";
                    customise.changingName = 1;
                    customise.typing = false;
                }
                if (customise.online)
                {
                    // Change IP
                    if (mouseOver(mx, my, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(customise.ip) - 10 - 30, Game.HEIGHT - 10 - 25, 30, 30, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
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
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
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
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                        Random r = new Random();
                        customise.playerColor[1] = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
                    }
                    // Player name 2
                    if (mouseOver(mx, my, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(customise.playerName[1]) - 10 - 30, Game.HEIGHT - 10 - 25, 30, 30, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                        customise.playerName[1] = "";
                        customise.changingName = 2;
                    }
                }
                // Board size 1 +
                if (mouseOver(mx, my, Game.WIDTH / 2 - 10 - 5 - 100, 120, 20, 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    if (customise.boardBigger) customise.boardSize[0]++;
                }
                // Board size 1 -
                if (mouseOver(mx, my, Game.WIDTH / 2 - 10 - 5 - 100, 120 + 20, 20, 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    if (customise.boardSmaller) customise.boardSize[0]--;
                }
                // Board size 2 +
                if (mouseOver(mx, my, Game.WIDTH / 2 + 10 + 100 - 20 + 1, 120, 20, 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    if (customise.boardBigger) customise.boardSize[1]++;
                }
                // Board size 2 -
                if (mouseOver(mx, my, Game.WIDTH / 2 + 10 + 100 - 20 + 1, 120 + 20, 20, 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    if (customise.boardSmaller) customise.boardSize[1]--;
                }
                break;
            case Settings :
                switch (settings.page)
                {
                    case 1:
                        // Theme Previous
                        if (mouseOver(mx, my, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                            if (Settings.settings.get("theme").equals("dark")) Settings.settings.replace("theme", "light");
                        }
                        // Theme Next
                        if (mouseOver(mx, my, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                            if (Settings.settings.get("theme").equals("light")) Settings.settings.replace("theme", "dark");
                        }
                        // Resolution Previous
                        if (mouseOver(mx, my, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 + 80 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                            if (Settings.settings.get("resolution").equals("1600x900"))
                            {
                                Settings.settings.replace("resolution", "1280x720");
                                break;
                            }
                            if (Settings.settings.get("resolution").equals("1280x720"))
                            {
                                Settings.settings.replace("resolution", "960x540");
                                break;
                            }
                        }
                        // Resolution Next
                        if (mouseOver(mx, my, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70  + 80 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                            if (Settings.settings.get("resolution").equals("960x540"))
                            {
                                Settings.settings.replace("resolution", "1280x720");
                                break;
                            }
                            if (Settings.settings.get("resolution").equals("1280x720"))
                            {
                                Settings.settings.replace("resolution", "1600x900");
                                break;
                            }
                        }
                        // Show FPS Previous
                        if (mouseOver(mx, my, Game.WIDTH / 2 - 290, Game.HEIGHT / 2 - 70 + 160 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                            if (Settings.settings.get("showfps").equals("true")) Settings.settings.replace("showfps", "false");
                        }
                        // Show FPS Next
                        if (mouseOver(mx, my, Game.WIDTH / 2 + 290 - 40, Game.HEIGHT / 2 - 70  + 160 - 13, 40, 40, false))
                        {
                            AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                            if (Settings.settings.get("showfps").equals("false")) Settings.settings.replace("showfps", "true");
                        }
                        break;
                }
                // Reset Settings
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.reset").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 50 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.reset").toUpperCase()), 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    settings.reset();
                }
                // Back
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("settings.back").toUpperCase()), 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    game.gameState = State.Menu;
                }
                // Previous Page
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<") / 2 - 100, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<"), 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    if (settings.page > 1) settings.page--;
                }
                // Next Page
                if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<") / 2 + 100, Game.HEIGHT - 50 - 10 + 35 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("<"), 20, false))
                {
                    AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                    if (settings.page < 2) settings.page++;
                }
                break;
            case Game :
                if (Game.PAUSED)
                {
                    // Continue button
                    if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Continue") / 2, Game.HEIGHT / 2 - 90 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Continue"), 20, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                        Game.PAUSED = false;
                    }
                    // Menu button
                    if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Menu") / 2, Game.HEIGHT / 2 - 60 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Menu"), 20, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                        handler.object.clear();
                        Game.PAUSED = false;
                        game.gameState = State.Menu;
                        Game.updateDiscord("In menu", "Main menu");
                    }
                    // Quit button
                    if (mouseOver(mx, my, Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Exit") / 2, Game.HEIGHT / 2 - 30 - 20, g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Exit"), 20, false))
                    {
                        AudioPlayer.playAudio("/audio/click.au", Float.parseFloat(Settings.settings.get("volume")));
                        System.exit(1);
                    }
                }
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        int mx = e.getX();
        int my = e.getY();

        if (game.gameState == State.Game)
        {
            if (!Game.PAUSED)
            {
                if (mx == startX && my == startY)
                {
                    if (!customise.online || hud.currentPlayer == 1)
                    {
                        for (GameObject object : handler.object)
                        {
                            if (object instanceof Tile)
                            {
                                if (mouseOver(mx, my, object.x, object.y, 25, 25, true))
                                {
                                    if (((Tile) object).player == 0)
                                    {
                                        ((Tile) object).player = hud.currentPlayer;
                                        System.out.println("Player " + hud.currentPlayer + " (" + customise.playerName[hud.currentPlayer - 1] + ") has taken tile " + ((Tile) object).posX + ", " + ((Tile) object).posY);

                                        hud.currentPlayer++;
                                        if (hud.currentPlayer > 2) hud.currentPlayer = 1;

                                        if (customise.online)
                                        {
                                            String x = String.valueOf(((Tile) object).posX), y = String.valueOf(((Tile) object).posY);
                                            while (x.length() < 3) x = "0" + x;
                                            while (y.length() < 3) y = "0" + y;

                                            switch (customise.onlineMode)
                                            {
                                                case 0 : game.client.sendUTF("t" + x + y); break;
                                                case 1 : game.server.sendUTF("t" + x + y); break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
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

        if (mouseOver(mx, my, 0, 0, Game.WIDTH, 36, false))
        {
            game.window.frame.setLocation(game.window.frame.getX() - (startX - mx), game.window.frame.getY() - (startY - my));
        }
        else
        {
            switch (game.gameState)
            {
                case Menu :
                    break;
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
                            Settings.settings.replace("volume", String.valueOf((mx - (float)(Game.WIDTH / 2 - 200)) / 400));
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