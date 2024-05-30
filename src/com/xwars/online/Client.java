package com.xwars.online;

import com.xwars.enums.MessageMode;
import com.xwars.gameobjects.Tile;
import com.xwars.main.Game;
import com.xwars.main.Handler;
import com.xwars.states.Customise;
import com.xwars.states.HUD;

import java.io.*;
import java.net.Socket;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for connecting to a server when playing online
 *
 * @author Soni
 * @version 1
 */
public class Client implements Runnable
{
    private final Game game;
    private final Customise customise;
    private final HUD hud;
    private final Handler handler;

    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    public Object data;
    public boolean connectionActive = false;

    private boolean running = true;
    private Thread thread;
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    
    /**
     * Constructor
     *
     * @param game The current Game object
     * @param customise Instance of the Customise class
     * @param hud Instance of the HUD class
     * @param handler The current Handler object
     */
    public Client(Game game, Customise customise, HUD hud, Handler handler)
    {
        this.game = game;
        this.customise = customise;
        this.hud = hud;
        this.handler = handler;
        
        LOGGER.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        LOGGER.addHandler(consoleHandler);
    }
    
    /**
     * Connects to a server at the specified IP address
     *
     * @param ip IP address to connect to
     */
    public void connect(String ip)
    {
        try
        {
            LOGGER.info("Starting online client");
            socket = new Socket(ip, Game.PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            LOGGER.info("Connected to server " + ip);
            connectionActive = true;
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, "Failed to connect to server " + ip, e);
        }
    }
    
    /**
     * Starts a new thread for the client to run on
     */
    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    
    /**
     * Stops the client exclusive thread
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
     * Game loop specifically for the client
     */
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
    
    /**
     * Sends data to the server
     *
     * @param object Data to send
     */
    public void send(Object object)
    {
        try
        {
            out.writeObject(object);
            LOGGER.info("Data sent to server (" + object + ")");
        }
        catch (IOException e)
        {
            LOGGER.log(Level.WARNING, "Failed to send data to server (" + object + ")", e);
        }
    }
    
    /**
     * Client tick loop, executes 60 times a second
     */
    public void tick()
    {
        try
        {
            data = in.readObject();
            LOGGER.info("Data received from server (" + data + ")");
    
            if (data instanceof Message message)
            {
                switch (message.mode)
                {
                    case Start:
                        customise.playerName[1] = message.name;
                        customise.playerColor[1] = message.color;
                        customise.boardSize = message.size;
                        hud.currentPlayer = 2;
                        Game.updateDiscord("In game", "Playing online");
                        game.startGame(message.foundation[0], message.foundation[1]);
                        
                        Message back = new Message();
                        back.mode = MessageMode.Start;
                        back.name = customise.playerName[0];
                        back.color = customise.playerColor[0];
    
                        send(back);
                        break;
                    case Tile:
                        Tile tile = handler.tiles[message.position[0]][message.position[1]];
    
                        tile.invaded = message.invade;
                        tile.player = tile.invaded ? (hud.currentPlayer + 1 == 3 ? 1 : 2) : hud.currentPlayer;
                        LOGGER.info("Player " + hud.currentPlayer + " (" + customise.playerName[hud.currentPlayer - 1] + ") has taken tile " + tile.posX + ", " + tile.posY);
                        
                        hud.changePlayer();
                }
            }
            else
            {
                LOGGER.warning("Could not deserialize data: Incorrect format.");
            }
        }
        catch (Exception ignored) {}
    }
}