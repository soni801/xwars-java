package com.xwars.main.input;

import com.xwars.main.Game;
import com.xwars.states.Customise;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Used to monitor key inputs to the application
 *
 * @author soni801
 */

public class KeyInput extends KeyAdapter
{
    private final Game game;
    private final Customise customise;

    public KeyInput(Game game, Customise customise)
    {
        this.game = game;
        this.customise = customise;
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        char keyChar = e.getKeyChar();

        switch (game.gameState)
        {
            case Customise :
                switch (customise.changingName)
                {
                    case 1 :
                        if (key == KeyEvent.VK_BACK_SPACE) customise.playerName[0] = customise.playerName[0].substring(0, customise.playerName[0].length() - 1);
                        else if (key == KeyEvent.VK_SHIFT || key == KeyEvent.VK_CAPS_LOCK) {}
                        else if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) customise.changingName = 0;
                        else customise.playerName[0] += keyChar;
                        break;
                    case 2 :
                        if (!customise.online)
                        {
                            if (key == KeyEvent.VK_BACK_SPACE) customise.playerName[1] = customise.playerName[1].substring(0, customise.playerName[1].length() - 1);
                            else if (key == KeyEvent.VK_SHIFT || key == KeyEvent.VK_CAPS_LOCK) {}
                            else if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) customise.changingName = 0;
                            else customise.playerName[1] += keyChar;
                        }
                        break;
                }
                if (customise.online)
                {
                    if (customise.typing)
                    {
                        if (key == KeyEvent.VK_BACK_SPACE) customise.ip = customise.ip.substring(0, customise.ip.length() - 1);
                        else if (key == KeyEvent.VK_SHIFT || key == KeyEvent.VK_CAPS_LOCK) {}
                        else if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) customise.typing = false;
                        else customise.ip += keyChar;
                    }
                }
                break;
            case Game :
                if (key == KeyEvent.VK_ESCAPE) Game.PAUSED = !Game.PAUSED;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}