package com.xwars.states;

/*
 * Author: soni801
 */

import com.xwars.main.Game;

import java.awt.*;

public class Customise
{
    private Game game;

    public boolean online = false;
    public int onlineMode = 0;
    public String ip;
    private boolean server = false;

    public Color[] playerColor = new Color[2];
    public String[] playerName = new String[2];
    public int[] boardSize = new int[2];

    public int colorPicker;
    public int r, g, b;

    public int changingName;
    public boolean typing;

    public boolean boardBigger, boardSmaller;

    public Customise(Game game)
    {
        this.game = game;

        playerColor[0] = Color.BLUE;
        playerColor[1] = Color.RED;

        playerName[0] = "PLAYER 1";
        playerName[1] = "PLAYER 2";

        ip = "<NO IP>";

        boardSize[0] = 50;
        boardSize[1] = 25;
    }

    public void tick()
    {
        boardBigger = !(boardSize[0] * boardSize[1] >= 5000);
        boardSmaller = !(boardSize[0] * boardSize[1] <= 100);
    }

    public void render(Graphics g)
    {
        g.setColor(new Color(120, 120, 120));
        g.setFont(Game.font.deriveFont(30f));

        g.drawString(Game.BUNDLE.getString("customise.back").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.back").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 + 35);

        if (!online) g.drawString(Game.BUNDLE.getString("customise.start").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.start").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 - 50 - 10 + 35);
        else
        {
            if (onlineMode == 1 && game.server.connectionActive) g.drawString(Game.BUNDLE.getString("customise.start").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.start").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 - 50 - 10 + 35);
            if (onlineMode == 0 && !game.client.connectionActive) g.drawString(Game.BUNDLE.getString("customise.connect").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.connect").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 - 50 - 10 + 35);
        }

        if (online)
        {
            g.drawString(Game.BUNDLE.getString("customise.play_offline").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.play_offline").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 - 50 - 10 - 50 - 10 + 35);
        }
        else
        {
            g.drawString(Game.BUNDLE.getString("customise.play_online").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.play_online").toUpperCase()) / 2, Game.HEIGHT - 50 - 10 - 50 - 10 - 50 - 10 + 35);
        }

        if (online)
        {
            if (onlineMode == 0) g.setColor(new Color(220, 220, 220)); else g.setColor(new Color(120, 120, 120));
            g.drawString(Game.BUNDLE.getString("customise.join_game").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.join_game").toUpperCase()) / 2, Game.HEIGHT / 2 + 35);
            if (onlineMode == 1) g.setColor(new Color(220, 220, 220)); else g.setColor(new Color(120, 120, 120));
            g.drawString(Game.BUNDLE.getString("customise.host_game").toUpperCase(), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.host_game").toUpperCase()) / 2, Game.HEIGHT / 2 - 50 - 10 + 35);

            if (onlineMode == 1)
            {
                if (!server)
                {
                    server = true;
                    game.server.start();
                }

                g.setColor(new Color(120, 120, 120));

                g.setFont(Game.font.deriveFont(15f));
                g.drawString(Game.BUNDLE.getString("customise.server_status").toUpperCase(), Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(15f)).stringWidth(Game.BUNDLE.getString("customise.server_status").toUpperCase()), Game.HEIGHT - 10 - 15 - 10);
                g.drawString(Game.BUNDLE.getString("customise.server_ip").toUpperCase(), Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(15f)).stringWidth(Game.BUNDLE.getString("customise.server_ip").toUpperCase()), Game.HEIGHT - 10 - 15 - 10 - 70);

                try
                {
                    g.setFont(Game.font.deriveFont(30f));
                    g.drawString(game.server.status, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(game.server.status), Game.HEIGHT - 10);
                    g.drawString(game.server.getIp(), Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(game.server.getIp()), Game.HEIGHT - 10 - 70);
                }
                catch (Exception ignored) {}
            }
            else
            {
                if (server)
                {
                    server = false;
                    game.server.stopServer();
                }

                g.setColor(new Color(120, 120, 120));

                g.setFont(Game.font.deriveFont(15f));
                if (!typing) g.drawString(Game.BUNDLE.getString("customise.server_ip").toUpperCase(), Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(15f)).stringWidth(Game.BUNDLE.getString("customise.server_ip").toUpperCase()), Game.HEIGHT - 10 - 15 - 10);
                else g.drawString(Game.BUNDLE.getString("customise.save").toUpperCase(), Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(15f)).stringWidth(Game.BUNDLE.getString("customise.save").toUpperCase()), Game.HEIGHT - 10 - 15 - 10);

                switch (Settings.settings.get("theme"))
                {
                    case "light" : g.setColor(Color.BLACK); break;
                    case "dark"  : g.setColor(Color.WHITE); break;
                }

                g.setFont(Game.font.deriveFont(30f));
                g.drawString(ip, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(ip), Game.HEIGHT - 10);

                g.setColor(new Color(120, 120, 120));

                if (typing)
                {
                    if (ip.equals(""))
                    {
                        g.setFont(Game.font.deriveFont(30f));
                        g.drawString(Game.BUNDLE.getString("customise.type_ip").toUpperCase(), Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.type_ip").toUpperCase()), Game.HEIGHT - 10);
                    }
                }
                else g.drawImage(game.pencil, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(ip) - 10 - 30, Game.HEIGHT - 10 - 25, 30, 30, null);
            }
        }
        else
        {
            if (server)
            {
                server = false;
                game.server.stopServer();
            }
        }

        switch (Settings.settings.get("theme"))
        {
            case "light" : g.setColor(Color.BLACK); break;
            case "dark"  : g.setColor(Color.WHITE); break;
        }

        g.drawString(Game.BUNDLE.getString("customise.boardsize"), Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.boardsize")) / 2, 100);
        g.drawRect(Game.WIDTH / 2 - 10 - 5 - 100, 120, 100, 40);
        g.drawRect(Game.WIDTH / 2 + 10, 120, 100, 40);

        switch (Settings.settings.get("theme"))
        {
            case "light" :
                g.drawImage(game.arrows_light, Game.WIDTH / 2 - 10 - 5 - 100, 120, null);
                g.drawImage(game.arrows_light, Game.WIDTH / 2 + 10 + 100 - 20 + 1, 120, null);
                break;
            case "dark" :
                g.drawImage(game.arrows_dark, Game.WIDTH / 2 - 10 - 5 - 100, 120, null);
                g.drawImage(game.arrows_dark, Game.WIDTH / 2 + 10 + 100 - 20 + 1, 120, null);
                break;
        }

        g.drawString(Integer.toString(boardSize[0]), Game.WIDTH / 2 - 10 - 5 - 100 + 20 + 10, 120 + 31);
        g.drawString(Integer.toString(boardSize[1]), Game.WIDTH / 2 + 10 + (100 - 20) - 5 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Integer.toString(boardSize[1])), 120 + 31);

        g.setFont(Game.font.deriveFont(20f));
        g.drawString("x", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(20f)).stringWidth("x") / 2, 120 + 26);

        g.setFont(Game.font.deriveFont(30f));
        g.drawString(playerName[0], 10, Game.HEIGHT - 10);
        if (!online) g.drawString(playerName[1], Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(playerName[1]), Game.HEIGHT - 10);

        g.setColor(new Color(0, 0, 0, .5f));

        if (!boardBigger)
        {
            g.fillRect(Game.WIDTH / 2 - 10 - 5 - 100, 120, 20, 20);
            g.fillRect(Game.WIDTH / 2 + 10 + 100 - 20 + 1, 120, 20, 20);
        }

        if (!boardSmaller)
        {
            g.fillRect(Game.WIDTH / 2 - 10 - 5 - 100, 120 + 20, 20, 20 + 1);
            g.fillRect(Game.WIDTH / 2 + 10 + 100 - 20 + 1, 120 + 20, 20, 20 + 1);
        }

        g.setColor(new Color(140, 140, 140));
        switch (changingName)
        {
            case 1 :
                if (playerName[0].equals(""))
                {
                    g.setFont(Game.font.deriveFont(30f));
                    g.drawString(Game.BUNDLE.getString("customise.type_name").toUpperCase(), 10, Game.HEIGHT - 10);
                }
                g.setFont(Game.font.deriveFont(15f));
                g.drawString(Game.BUNDLE.getString("customise.save").toUpperCase(), 10, Game.HEIGHT - 10 - 15 - 10);
                break;
            case 2 :
                if (playerName[1].equals(""))
                {
                    g.setFont(Game.font.deriveFont(30f));
                    g.drawString(Game.BUNDLE.getString("customise.type_name").toUpperCase(), Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(Game.BUNDLE.getString("customise.type_name").toUpperCase()), Game.HEIGHT - 10);
                }
                g.setFont(Game.font.deriveFont(15f));
                g.drawString(Game.BUNDLE.getString("customise.save").toUpperCase(), Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(15f)).stringWidth(Game.BUNDLE.getString("customise.save").toUpperCase()), Game.HEIGHT - 10 - 15 - 10);
                break;
            default :
                g.drawImage(game.pencil, 10 + g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(playerName[0]) + 10, Game.HEIGHT - 10 - 25, 30, 30, null);
                if (!online) g.drawImage(game.pencil, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(playerName[1]) - 10 - 30, Game.HEIGHT - 10 - 25, 30, 30, null);

                g.setColor(playerColor[0]);
                g.fillRect(10, Game.HEIGHT - 10 - 60, 30, 30);
                g.drawImage(game.pencil, 10 + 30 + 10, Game.HEIGHT - 10 - 60, 30, 30, null);
                g.drawImage(game.dice, 10 + 30 + 10 + 30 + 10, Game.HEIGHT - 10 - 60, 30, 30, null);

                if (!online)
                {
                    g.setColor(playerColor[1]);
                    g.fillRect(Game.WIDTH - 10 - 30, Game.HEIGHT - 10 - 60, 30, 30);
                    g.drawImage(game.pencil, Game.WIDTH - 10 - 30 - 10 - 30, Game.HEIGHT - 10 - 60, 30, 30, null);
                    g.drawImage(game.dice, Game.WIDTH - 10 - 30 - 10 - 30 - 10 - 30, Game.HEIGHT - 10 - 60, 30, 30, null);
                }

                switch (colorPicker)
                {
                    case 1 :
                        g.setColor(new Color(0, 0,  0, .5f));
                        g.fillRect(10, Game.HEIGHT - 100 - 10 - 120, 30 + 255 + 30, 120);

                        g.setColor(Color.WHITE);
                        g.drawLine(10 + 30, Game.HEIGHT - 100 - 10 - 120 + 30, 10 + 30 + 255, Game.HEIGHT - 100 - 10 - 120 + 30);
                        g.drawLine(10 + 30, Game.HEIGHT - 100 - 10 - 120 + 60, 10 + 30 + 255, Game.HEIGHT - 100 - 10 - 120 + 60);
                        g.drawLine(10 + 30, Game.HEIGHT - 100 - 10 - 120 + 90, 10 + 30 + 255, Game.HEIGHT - 100 - 10 - 120 + 90);

                        g.fillRect(10 + 30 + this.r - 5, Game.HEIGHT - 100 - 10 - 120 + 30 - 10, 10, 20);
                        g.fillRect(10 + 30 + this.g - 5, Game.HEIGHT - 100 - 10 - 120 + 60 - 10, 10, 20);
                        g.fillRect(10 + 30 + this.b - 5, Game.HEIGHT - 100 - 10 - 120 + 90 - 10, 10, 20);

                        playerColor[0] = new Color(r, this.g, b);
                        break;
                    case 2 :
                        g.setColor(new Color(0, 0,  0, .5f));
                        g.fillRect(Game.WIDTH - 15 - (30 + 255 + 30), Game.HEIGHT - 100 - 10 - 120, 30 + 255 + 30, 120);

                        g.setColor(Color.WHITE);
                        g.drawLine(Game.WIDTH - 15 - (30 + 255 + 30) + 30, Game.HEIGHT - 100 - 10 - 120 + 30, Game.WIDTH - 15 - (30 + 255 + 30) + 30 + 255, Game.HEIGHT - 100 - 10 - 120 + 30);
                        g.drawLine(Game.WIDTH - 15 - (30 + 255 + 30) + 30, Game.HEIGHT - 100 - 10 - 120 + 60, Game.WIDTH - 15 - (30 + 255 + 30) + 30 + 255, Game.HEIGHT - 100 - 10 - 120 + 60);
                        g.drawLine(Game.WIDTH - 15 - (30 + 255 + 30) + 30, Game.HEIGHT - 100 - 10 - 120 + 90, Game.WIDTH - 15 - (30 + 255 + 30) + 30 + 255, Game.HEIGHT - 100 - 10 - 120 + 90);

                        g.fillRect(Game.WIDTH - 15 - (30 + 255 + 30) + 30 + this.r - 5, Game.HEIGHT - 100 - 10 - 120 + 30 - 10, 10, 20);
                        g.fillRect(Game.WIDTH - 15 - (30 + 255 + 30) + 30 + this.g - 5, Game.HEIGHT - 100 - 10 - 120 + 60 - 10, 10, 20);
                        g.fillRect(Game.WIDTH - 15 - (30 + 255 + 30) + 30 + this.b - 5, Game.HEIGHT - 100 - 10 - 120 + 90 - 10, 10, 20);

                        playerColor[1] = new Color(r, this.g, b);
                        break;
                }
        }
    }
}