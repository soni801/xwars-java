package com.xwars.main;

/*
 * Author: soni801
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter
{
    private Game game;
    private Customise customise;

    public KeyInput(Game game, Customise customise)
    {
        this.game = game;
        this.customise = customise;
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (game.gameState)
        {
            case Customise :
                switch (customise.changingName)
                {
                    case 1 :
                        if (key == KeyEvent.VK_BACK_SPACE) customise.playerName[0] = customise.playerName[0].substring(0, customise.playerName[0].length() - 1);
                        else customise.playerName[0] += (char)key;
                        break;
                    case 2 :
                        if (key == KeyEvent.VK_BACK_SPACE) customise.playerName[1] = customise.playerName[1].substring(0, customise.playerName[1].length() - 1);
                        else customise.playerName[1] += (char)key;
                        break;
                }
                if (key == KeyEvent.VK_ENTER) customise.changingName = 0;
            case Game :
                if (key == KeyEvent.VK_ESCAPE) Game.PAUSED = !Game.PAUSED;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
    }
}