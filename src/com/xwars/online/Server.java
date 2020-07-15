package com.xwars.online;

import com.xwars.main.Game;
import com.xwars.states.Customise;

import java.awt.*;
import java.io.*;
import java.net.*;

/**
 * The <code>Server</code> class is used for hosting online play
 *
 * @author soni801
 */

public class Server implements Runnable
{
    private Customise customise;

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

    public Server(Customise customise)
    {
        this.customise = customise;
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
        catch (IOException e)
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
                    break;
                case "t":
                    break;
            }
        }
        catch (Exception ignored) {}
    }
}