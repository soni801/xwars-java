package com.xwars.online;

import com.xwars.main.Game;

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

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public String input;
    public boolean connectionActive = false;

    private boolean running = true;
    private Thread thread;

    public Client(Game game)
    {
        this.game = game;
    }

    public void connect(String ip)
    {
        try
        {
            System.out.println("Starting client...");
            socket = new Socket(ip, Game.PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected to server " + ip);
            connectionActive = true;
        }
        catch (IOException e)
        {
            System.out.println("Failed to connect to server " + ip);
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
            System.out.println("Message sent to client: " + str);
        }
        catch (IOException e)
        {
            System.out.println("Failed to send message to client: " + str);
        }
    }

    public void tick()
    {
        try
        {
            input = in.readUTF();
            System.out.println("Message from server: " + input);

            switch (input.substring(0, 1))
            {
                case "s":
                    game.startGame();
            }
        }
        catch (Exception ignored) {}
    }
}