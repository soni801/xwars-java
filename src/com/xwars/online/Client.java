package com.xwars.online;

import com.xwars.gameobjects.Tile;
import com.xwars.main.Game;
import com.xwars.main.Handler;
import com.xwars.states.Customise;
import com.xwars.states.HUD;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The <code>Client</code> class is used for joining online play
 *
 * @author soni801
 */

public class Client implements Runnable
{
    private final Game game;
    private final Customise customise;
    private final HUD hud;
    private final Handler handler;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public String input;
    public boolean connectionActive = false;

    private boolean running = true;
    private Thread thread;

    public Client(Game game, Customise customise, HUD hud, Handler handler)
    {
        this.game = game;
        this.customise = customise;
        this.hud = hud;
        this.handler = handler;
    }

    public void connect(String ip)
    {
        try
        {
            System.out.println("Starting client...");
            socket = new Socket(ip, Game.PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("[CLIENT] Connected to server " + ip);
            connectionActive = true;
        }
        catch (IOException e)
        {
            System.out.println("[CLIENT] Failed to connect to server " + ip);
        }
    }

    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop()
    {
        try
        {
            thread.join();
            running = false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        while (running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1)
            {
                tick();
                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
            }
        }
        stop();
    }

    public void sendUTF(String str)
    {
        /*
         * "i": Player info
         *      + length of player name (2)
         *      + player name
         *      + player color r (3)
         *      + player color g (3)
         *      + player color b (3)
         *
         * "t": Place tile
         *      + tile pos x (3)
         *      + tile pos y (3)
         */

        try
        {
            out.writeUTF(str);
            System.out.println("[CLIENT] Message sent to server: " + str);
        }
        catch (IOException e)
        {
            System.out.println("[CLIENT] Failed to send message to server: " + str);
        }
    }

    public void tick()
    {
        try
        {
            input = in.readUTF();
            System.out.println("[CLIENT] Received message (" + input + ")");

            switch (input.substring(0, 1))
            {
                case "s":
                    // Decode info
                    String name;
                    int r, g, b;
                    int y1, y2;
                    
                    name = input.substring(3, Integer.parseInt(input.substring(1, 3)) + 3);
                    r = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 3, Integer.parseInt(input.substring(1, 3)) + 3 + 3));
                    g = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 6, Integer.parseInt(input.substring(1, 3)) + 6 + 3));
                    b = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 9, Integer.parseInt(input.substring(1, 3)) + 9 + 3));
                    y1 = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 9 + 3, Integer.parseInt(input.substring(1, 3)) + 9 + 3 + 3));
                    y2 = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 9 + 6, Integer.parseInt(input.substring(1, 3)) + 9 + 6 + 3));

                    System.out.printf("[CLIENT] Decoded message:\n\tStart Game\n\tPlayer name: %s\n\tPlayer color: (%d, %d, %d)\n\tFoundation 1 position: %d\n\tFoundation 2 position: %d\n", name, r, g, b, y1, y2);

                    customise.playerName[1] = name;
                    customise.playerColor[1] = new Color(r, g, b);

                    hud.currentPlayer = 2;
                    Game.updateDiscord("In game", "Playing online");
    
                    game.startGame(y1, y2);
                    
                    // Send back info
                    System.out.println("[CLIENT] Sending info back to server");

                    String nameLength = String.valueOf(customise.playerName[0].length());
                    while (nameLength.length() < 2) nameLength = "0" + nameLength;

                    name = customise.playerName[0];

                    String rs = String.valueOf(customise.playerColor[0].getRed()), gs = String.valueOf(customise.playerColor[0].getGreen()), bs = String.valueOf(customise.playerColor[0].getBlue());
                    while (rs.length() < 3) rs = "0" + rs;
                    while (gs.length() < 3) gs = "0" + gs;
                    while (bs.length() < 3) bs = "0" + bs;

                    sendUTF("i" + nameLength + name + rs + gs + bs);
                    break;
                case "t":
                    int x, y;

                    x = Integer.parseInt(input.substring(1, 4));
                    y = Integer.parseInt(input.substring(4, 7));

                    System.out.printf("[CLIENT] Decoded message:\n\tPlace tile\n\tTile position: (%d, %d)\n", x, y);
    
                    Tile tile = handler.tiles[x][y];
                    
                    tile.player = hud.currentPlayer;
                    System.out.println("Player " + hud.currentPlayer + " (" + customise.playerName[hud.currentPlayer - 1] + ") has taken tile " + tile.posX + ", " + tile.posY);
                    
                    hud.changePlayer();
                    break;
            }
        }
        catch (Exception ignored) {}
    }
}