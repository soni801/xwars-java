package com.xwars.online;

import com.xwars.gameobjects.Tile;
import com.xwars.main.Game;
import com.xwars.main.Handler;
import com.xwars.states.Customise;
import com.xwars.states.HUD;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 * The <code>Server</code> class is used for hosting online play
 *
 * @author soni801
 */

public class Server implements Runnable
{
    private final Customise customise;
    private final HUD hud;
    private final Handler handler;

    ServerSocket serverSocket;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    private String ip;
    private boolean running = true;
    private Thread thread;

    public String input;
    public String status;
    public boolean connectionActive = false;

    public String getIp() { return ip; }

    public Server(Customise customise, HUD hud, Handler handler)
    {
        this.customise = customise;
        this.hud = hud;
        this.handler = handler;
    }

    public void sendUTF(String str)
    {
        /*
         * "s": Start Game
         *      + length of player name (2)
         *      + player name
         *      + player color r (3)
         *      + player color g (3)
         *      + player color b (3)
         *      + foundation 1 y (3)
         *      + foundation 2 y (3)
         *
         * "t": Place tile
         *      + tile pos x (3)
         *      + tile pos y (3)
         */

        try
        {
            out.writeUTF(str);
            System.out.println("[SERVER] Message sent to client: " + str);
        }
        catch (IOException e)
        {
            System.out.println("[SERVER] Failed to send message to client: " + str);
        }
    }

    public void stopServer()
    {
        try
        {
            serverSocket.close();
            System.out.println("Server closed");
        }
        catch (IOException | NullPointerException e)
        {
            System.out.println("Failed to close server");
        }
    }

    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public void run()
    {
        try
        {
            // Get public IP
            URL ipCheck = new URL("http://checkip.amazonaws.com");
            BufferedReader reader = new BufferedReader(new InputStreamReader(ipCheck.openStream()));
            ip = reader.readLine();

            // Start server
            status = "Starting server on port " + Game.PORT + "...";
            System.out.println(status);
            serverSocket = new ServerSocket(Game.PORT);
            System.out.println("Server IP: " + ip);
            status = "Waiting for connection...";
            System.out.println("[SERVER] " + status);
            socket = serverSocket.accept();
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            status = "Client connected";
            connectionActive = true;
            System.out.println("[SERVER] " + status + " from " + socket.getInetAddress());
        }
        catch (IOException e)
        {
            status = "Failed to start server";
            System.out.println(status);
        }

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

    public void tick()
    {
        try
        {
            input = in.readUTF();
            System.out.println("[SERVER] Received message (" + input + ")");

            switch (input.substring(0, 1))
            {
                case "i":
                    // Decode info
                    String name;
                    int r, g, b;

                    name = input.substring(3, Integer.parseInt(input.substring(1, 3)) + 3);
                    r = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 3, Integer.parseInt(input.substring(1, 3)) + 3 + 3));
                    g = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 6, Integer.parseInt(input.substring(1, 3)) + 6 + 3));
                    b = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 9, Integer.parseInt(input.substring(1, 3)) + 9 + 3));

                    System.out.printf("[SERVER] Decoded message:\n\tPlayer info\n\tPlayer name: %s\n\tPlayer color: (%d, %d, %d)\n", name, r, g, b);

                    customise.playerName[1] = name;
                    customise.playerColor[1] = new Color(r, g, b);

                    Game.updateDiscord("In game", "Playing online");
                    break;
                case "t":
                    int x, y;

                    x = Integer.parseInt(input.substring(1, 4));
                    y = Integer.parseInt(input.substring(4, 7));

                    System.out.printf("[SERVER] Decoded message:\n\tPlace tile\n\tTile position: (%d, %d)\n", x, y);
    
                    for (Tile[] tileArray : handler.tiles)
                    {
                        for (Tile tile : tileArray)
                        {
                            if (tile.posX == x && tile.posY == y)
                            {
                                tile.player = hud.currentPlayer;
                                System.out.println("Player " + hud.currentPlayer + " (" + customise.playerName[hud.currentPlayer - 1] + ") has taken tile " + tile.posX + ", " + tile.posY);
        
                                hud.currentPlayer = 1;
                            }
                        }
                    }
                    break;
            }
        }
        catch (Exception ignored) {}
    }
}