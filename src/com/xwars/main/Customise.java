package com.xwars.main;

/*
 * Author: soni801
 */

import java.awt.*;

public class Customise
{
    private Game game;

    Color[] playerColor = new Color[2];
    String[] playerName = new String[2];
    int[] boardSize = new int[2];

    int colorPicker;
    int r, g, b;

    int changingName;

    boolean boardBigger, boardSmaller;

    public Customise(Game game)
    {
        this.game = game;

        playerColor[0] = Color.BLUE;
        playerColor[1] = Color.RED;

        playerName[0] = "PLAYER 1";
        playerName[1] = "PLAYER 2";

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

        g.drawString("BACK", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("BACK") / 2, Game.HEIGHT - 50 - 10 + 35);
        g.drawString("START", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("START") / 2, Game.HEIGHT - 50 - 10 - 50 - 10 + 35);

        switch (Settings.settings.get("theme"))
        {
            case "light" : g.setColor(Color.BLACK); break;
            case "dark"  : g.setColor(Color.WHITE); break;
        }

        g.drawString("Board Size", Game.WIDTH / 2 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("Board Size") / 2, 100);
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
        g.drawString(playerName[1], Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(playerName[1]), Game.HEIGHT - 10);

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
                    g.drawString("TYPE A NAME", 10, Game.HEIGHT - 10);
                }
                g.setFont(Game.font.deriveFont(15f));
                g.drawString("ENTER TO SAVE", 10, Game.HEIGHT - 10 - 15 - 10);
                break;
            case 2 :
                if (playerName[1].equals(""))
                {
                    g.setFont(Game.font.deriveFont(30f));
                    g.drawString("TYPE A NAME", Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth("TYPE A NAME"), Game.HEIGHT - 10);
                }
                g.setFont(Game.font.deriveFont(15f));
                g.drawString("ENTER TO SAVE", Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(15f)).stringWidth("ENTER TO SAVE"), Game.HEIGHT - 10 - 15 - 10);
                break;
            default :
                g.drawImage(game.pencil, 10 + g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(playerName[0]) + 10, Game.HEIGHT - 10 - 25, 30, 30, null);
                g.drawImage(game.pencil, Game.WIDTH - 10 - g.getFontMetrics(Game.font.deriveFont(30f)).stringWidth(playerName[1]) - 10 - 30, Game.HEIGHT - 10 - 25, 30, 30, null);

                g.setColor(playerColor[0]);
                g.fillRect(10, Game.HEIGHT - 10 - 60, 30, 30);
                g.drawImage(game.pencil, 10 + 30 + 10, Game.HEIGHT - 10 - 60, 30, 30, null);
                g.drawImage(game.dice, 10 + 30 + 10 + 30 + 10, Game.HEIGHT - 10 - 60, 30, 30, null);

                g.setColor(playerColor[1]);
                g.fillRect(Game.WIDTH - 10 - 30, Game.HEIGHT - 10 - 60, 30, 30);
                g.drawImage(game.pencil, Game.WIDTH - 10 - 30 - 10 - 30, Game.HEIGHT - 10 - 60, 30, 30, null);
                g.drawImage(game.dice, Game.WIDTH - 10 - 30 - 10 - 30 - 10 - 30, Game.HEIGHT - 10 - 60, 30, 30, null);

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