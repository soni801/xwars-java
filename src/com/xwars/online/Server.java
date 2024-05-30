package com.xwars.online;

import com.xwars.gameobjects.Tile;
import com.xwars.main.Game;
import com.xwars.main.Handler;
import com.xwars.states.Customise;
import com.xwars.states.HUD;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for hosting online play
 *
 * @author Soni
 * @version 1
 */
public class Server implements Runnable
{
    private final Customise customise;
    private final HUD hud;
    private final Handler handler;

    ServerSocket serverSocket;
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    private String ip;
    private boolean running = true;
    private Thread thread;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public Object data;
    public String status;
    public boolean connectionActive = false;

    public String getIp() { return ip; }
    
    /**
     * Constructor
     *
     * @param customise Instance of the Customise class
     * @param hud Instance of the HUD class
     * @param handler The current Handler object
     */
    public Server(Customise customise, HUD hud, Handler handler)
    {
        this.customise = customise;
        this.hud = hud;
        this.handler = handler;
        
        LOGGER.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        LOGGER.addHandler(consoleHandler);
    }
    
    /**
     * Sends data to the client
     *
     * @param object Data to send
     */
    public void send(Object object)
    {
        try
        {
            out.writeObject(object);
            LOGGER.info("Data sent to client (" + object + ")");
        }
        catch (IOException e)
        {
            LOGGER.log(Level.WARNING, "Failed to send data to client (" + object + ")", e);
        }
    }
    
    /**
     * Stops the serverSocket
     */
    public void stopServer()
    {
        try
        {
            serverSocket.close();
            LOGGER.info("Server closed");
        }
        catch (IOException | NullPointerException e)
        {
            LOGGER.log(Level.WARNING, "Failed to close server", e);
        }
    }
    
    /**
     * Creates a new thread for the server to run on
     */
    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    
    /**
     * Game loop specifically for the server
     */
    public void run()
    {
        try
        {
            // Get public IP
            URL ipCheck = new URL("https://checkip.amazonaws.com");
            BufferedReader reader = new BufferedReader(new InputStreamReader(ipCheck.openStream()));
            ip = reader.readLine();

            // Start server
            status = "Starting server on port " + Game.PORT + "...";
            LOGGER.info(status);
            serverSocket = new ServerSocket(Game.PORT);
            LOGGER.fine("Server IP: " + ip);
            status = "Waiting for connection...";
            LOGGER.info(status);
            socket = serverSocket.accept();
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            status = "Client connected";
            connectionActive = true;
            LOGGER.info(status + " from " + socket.getInetAddress());
        }
        catch (IOException e)
        {
            status = "Failed to start server";
            LOGGER.log(Level.WARNING, status, e);
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
    
    /**
     * Stops the server exclusive thread
     */
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
    
    /**
     * Server tick loop, executes 60 times a second
     */
    public void tick()
    {
        try
        {
            data = in.readObject();
            LOGGER.info("Data received from client (" + data + ")");
            
            if (data instanceof Message message)
            {
                switch (message.mode)
                {
                    case Start:
                        customise.playerName[1] = message.name;
                        customise.playerColor[1] = message.color;
                        Game.updateDiscord("In game", "Playing online");
                        break;
                    case Tile:
                        Tile tile = handler.tiles[message.position[0]][message.position[1]];
                        
                        tile.invaded = message.invade;
                        tile.player = tile.invaded ? (hud.currentPlayer + 1 == 3 ? 1 : 2) : hud.currentPlayer;
                        LOGGER.fine("Player " + hud.currentPlayer + " (" + customise.playerName[hud.currentPlayer - 1] + ") has taken tile " + tile.posX + ", " + tile.posY);
                        
                        hud.changePlayer();
                }
            }
            else
            {
                LOGGER.warning("Error occurred while deserializing data: Data is not correct format.");
            }
        }
        catch (Exception ignored) {}
    }
}