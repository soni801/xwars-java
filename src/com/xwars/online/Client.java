package com.xwars.online;

import com.xwars.main.Game;
import com.xwars.states.Customise;

import java.awt.*;
import java.io.*;
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

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public String input;
    public boolean connectionActive = false;

    private boolean running = true;
    private Thread thread;

    public Client(Game game, Customise customise)
    {
        this.game = game;
        this.customise = customise;
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
                    String name;
                    int r, g, b;

                    game.startGame();
                    name = input.substring(3, Integer.parseInt(input.substring(1, 3)) + 3);
                    r = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 3, Integer.parseInt(input.substring(1, 3)) + 3 + 3));
                    g = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 6, Integer.parseInt(input.substring(1, 3)) + 6 + 3));
                    b = Integer.parseInt(input.substring(Integer.parseInt(input.substring(1, 3)) + 9, Integer.parseInt(input.substring(1, 3)) + 9 + 3));

                    System.out.printf("[CLIENT] Decoded message:\n\tStart Game\n\tPlayer name: %s\n\tPlayer color: (%d, %d, %d)\n", name, r, g, b);

                    customise.playerName[1] = name;
                    customise.playerColor[1] = new Color(r, g, b);
                    break;
                case "t":
                    break;
            }
        }
        catch (Exception ignored) {}
    }
}