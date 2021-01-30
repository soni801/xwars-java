package com.xwars.main;

import com.xwars.gameobjects.Tile;
import com.xwars.main.input.KeyInput;
import com.xwars.main.input.MouseInput;
import com.xwars.main.loaders.ResourceLoader;
import com.xwars.online.Client;
import com.xwars.online.Message;
import com.xwars.online.Server;
import com.xwars.states.*;
import com.xwars.states.Menu;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Contains the main method along with other core elements
 * of the application, like the game loop and instance management.
 *
 * @author Soni
 * @version beta-0.2.2
 */
public class Game extends Canvas implements Runnable
{
    public static final String BRAND = "Redsea Productions";
    public static final String PRODUCT = "The Great X Wars";
    public static final String VERSION = "beta-0.2.2";
    
    public static int WIDTH, HEIGHT;
    public static long firstTick = System.currentTimeMillis();

    public static ResourceBundle BUNDLE;

    private final Handler handler;
    private Thread thread;

    private boolean running = false;
    public static boolean PAUSED = false;
    public static boolean ready = false;

    public int selected_close_operation;
    private String fps;

    public ResourceLoader resourceLoader;
    
    public BufferedImage icon;
    public BufferedImage redsea;
    public BufferedImage dice;
    public BufferedImage pencil;

    public BufferedImage close_operations_default;
    public BufferedImage close_operations_close_select_dark;
    public BufferedImage close_operations_close_select_light;
    public BufferedImage close_operations_minimise_select_dark;
    public BufferedImage close_operations_minimise_select_light;

    public BufferedImage arrows_dark;
    public BufferedImage arrows_light;

    public BufferedImage arrow_left;
    public BufferedImage arrow_right;

    private final MouseInput mouseInput;
    private final HUD hud;
    private final Settings settings;
    private final Menu menu;
    private final Customise customise;
    private final Rules rules;
    private final Win win;

    public State gameState = State.Menu;

    public Server server;
    public Client client;
    public static int PORT = 14242;

    public static Font font;

    public Window window;
    
    /**
     * Constructor used in the main method to initialise the application.
     * The contents of this constructor will be executed upon application launch.
     */
    public Game()
    {
        resourceLoader = new ResourceLoader();
        
        // Load Font
        font = resourceLoader.loadFont("fonts/font.ttf");

        // Show loading window
        Window.showLoading();

        // Load images
        icon = resourceLoader.loadImage("/images/icon.png");
        redsea = resourceLoader.loadImage("/images/redsea.png");
        dice = resourceLoader.loadImage("/images/dice.png");
        pencil = resourceLoader.loadImage("/images/pencil.png");

        close_operations_default = resourceLoader.loadImage("/images/close_operations/default.png");
        close_operations_close_select_dark = resourceLoader.loadImage("/images/close_operations/close_select_dark.png");
        close_operations_close_select_light = resourceLoader.loadImage("/images/close_operations/close_select_light.png");
        close_operations_minimise_select_dark = resourceLoader.loadImage("/images/close_operations/minimise_select_dark.png");
        close_operations_minimise_select_light = resourceLoader.loadImage("/images/close_operations/minimise_select_light.png");

        arrows_dark = resourceLoader.loadImage("/images/arrows_dark.png");
        arrows_light = resourceLoader.loadImage("/images/arrows_light.png");

        arrow_left = resourceLoader.loadImage("/images/settings/arrow_left.png");
        arrow_right = resourceLoader.loadImage("/images/settings/arrow_right.png");

        System.out.println("Loaded resources");

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            public void run()
            {
                DiscordRPC.discordShutdown();
                System.out.println("Disconnected from Discord");
    
                settings.save();
    
                server.stopServer();
            }
        }, "Shutdown-thread"));

        // Initialise objects
        handler = new Handler();
    
        settings = new Settings(this);
        customise = new Customise(this, settings);
        win = new Win(settings, customise);
        hud = new HUD(handler, customise, settings, this, win);
        menu = new Menu(this, settings);
        rules = new Rules(settings);

        server = new Server(customise, hud, handler);
        client = new Client(this, customise, hud, handler);

        mouseInput = new MouseInput(resourceLoader, handler, hud, this, customise, settings, rules);

        System.out.println("Initialised objects");

        // Add input listeners
        this.addKeyListener(new KeyInput(this, customise));
        this.addMouseListener(mouseInput);
        this.addMouseMotionListener(mouseInput);
        this.addMouseWheelListener(mouseInput);

        System.out.println("Configured input listeners");

        // Load settings
        settings.load();
        
        try
        {
            switch (settings.get("resolution"))
            {
                case "540"    -> WIDTH = 960;
                case "720"   -> WIDTH = 1280;
                case "900"   -> WIDTH = 1600;
                case "fullscreen" -> WIDTH = 1;
                default -> {
                    WIDTH = 1280;
                    System.out.println("Could not load resolution correctly. Using default resolution at 1280x720.");
                }
            }
            HEIGHT = WIDTH / 16 * 9;

            BUNDLE = resourceLoader.loadBundle("lang.lang_" + settings.get("language"));
        }
        catch (Exception e)
        {
            settings.reset();

            JOptionPane.showMessageDialog(null, "Failed to load settings. Please re-launch the game", "Error", JOptionPane.INFORMATION_MESSAGE);

            System.exit(1);
        }

        // Initialise Discord
        initDiscord();
        for (int i = 0; !ready; i++)
        {
            DiscordRPC.discordRunCallbacks();
            if (i > 8000000)
            {
                System.out.println("Discord connection failed: Timed out");
                break;
            }
        }

        // Start game
        System.out.println("Starting in resolution " + WIDTH + "x" + HEIGHT);
        window = new Window(WIDTH, HEIGHT, "The Great X Wars", this);

        System.out.println("Startup complete.");
    }
    
    /**
     * Starts the application by creating and starting a thread
     */
    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    
    /**
     * Stops the application
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
     * Constantly loops, containing the game loop.
     * The game loop ticks 60 times a second and executes the render method,
     * as well as keeping count of the current application framerate.
     */
    public void run()
    {
        DiscordRPC.discordRunCallbacks();
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
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
            if (running)
                render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                fps = String.valueOf(frames);
                frames = 0;
            }
        }
        stop();
    }
    
    /**
     * Starts the game and sends foundation information to the client.
     *
     * @param pos1 The position of the first foundation unit
     * @param pos2 The position of the second foundation unit
     */
    public void startGame(int pos1, int pos2)
    {
        handler.tiles = new Tile[customise.boardSize[0]][customise.boardSize[1]];
        hud.generate(customise.boardSize[0], customise.boardSize[1]);
        
        if (customise.online)
        {
            if (customise.onlineMode == 1)
            {
                int[] foundationPos = createFoundations();
    
                try
                {
                    if (server.connectionActive)
                    {
                        Message message = new Message();
                        message.mode = "start";
                        message.name = customise.playerName[0];
                        message.color = customise.playerColor[0];
                        message.size = customise.boardSize;
                        message.foundation = foundationPos;
                        
                        server.send(message);
                    }
                }
                catch (Exception ignored) {}
            }
            else
            {
                createFoundations(pos1, pos2);
            }
        }
        else
        {
            createFoundations();
        }
        
        gameState = State.Game;
        hud.initialise();
    }
    
    /**
     * Generates foundation units at random vertical positions
     *
     * @return Foundation units positions
     */
    private int[] createFoundations()
    {
        Random r = new Random();
        int y1 = r.nextInt(customise.boardSize[1] - 2);
        int y2 = r.nextInt(customise.boardSize[1] - 2);
        
        handler.tiles[0][y1].player = 1;
        handler.tiles[0][y1].foundation = 1;
    
        handler.tiles[1][y1].player = 1;
        handler.tiles[1][y1].foundation = 2;
    
        handler.tiles[0][y1 + 1].player = 1;
        handler.tiles[0][y1 + 1].foundation = 3;
    
        handler.tiles[1][y1 + 1].player = 1;
        handler.tiles[1][y1 + 1].foundation = 4;
    
        handler.tiles[customise.boardSize[0] - 2][y2].player = 2;
        handler.tiles[customise.boardSize[0] - 2][y2].foundation = 1;
    
        handler.tiles[customise.boardSize[0] - 1][y2].player = 2;
        handler.tiles[customise.boardSize[0] - 1][y2].foundation = 2;
    
        handler.tiles[customise.boardSize[0] - 2][y2 + 1].player = 2;
        handler.tiles[customise.boardSize[0] - 2][y2 + 1].foundation = 3;
    
        handler.tiles[customise.boardSize[0] - 1][y2 + 1].player = 2;
        handler.tiles[customise.boardSize[0] - 1][y2 + 1].foundation = 4;
        
        return new int[]{y1, y2};
    }
    
    /**
     * Generates foundation units at set vertical positions
     *
     * @param y1 Vertical position of first foundation unit
     * @param y2 Vertical position of second foundation unit
     */
    private void createFoundations(int y1, int y2)
    {
        handler.tiles[0][y1].player = 2;
        handler.tiles[0][y1].foundation = 1;
        
        handler.tiles[1][y1].player = 2;
        handler.tiles[1][y1].foundation = 2;
        
        handler.tiles[0][y1 + 1].player = 2;
        handler.tiles[0][y1 + 1].foundation = 3;
        
        handler.tiles[1][y1 + 1].player = 2;
        handler.tiles[1][y1 + 1].foundation = 4;
        
        handler.tiles[customise.boardSize[0] - 2][y2].player = 1;
        handler.tiles[customise.boardSize[0] - 2][y2].foundation = 1;
        
        handler.tiles[customise.boardSize[0] - 1][y2].player = 1;
        handler.tiles[customise.boardSize[0] - 1][y2].foundation = 2;
        
        handler.tiles[customise.boardSize[0] - 2][y2 + 1].player = 1;
        handler.tiles[customise.boardSize[0] - 2][y2 + 1].foundation = 3;
        
        handler.tiles[customise.boardSize[0] - 1][y2 + 1].player = 1;
        handler.tiles[customise.boardSize[0] - 1][y2 + 1].foundation = 4;
    }
    
    /**
     * Executes 60 times a second. Makes sure that the current menu gets updated
     */
    private void tick()
    {
        switch (gameState)
        {
            case Menu      -> menu.tick();
            case Customise -> customise.tick();
        }
    }
    
    /**
     * Renders objects on screen
     */
    public void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        int offX = mouseInput.dragX;
        int offY = mouseInput.dragY;
    
        switch (settings.get("theme"))
        {
            case "light" -> g.setColor(Color.WHITE);
            case "dark"  -> g.setColor(Color.DARK_GRAY);
        }

        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.translate(-offX, -offY);

        handler.render(g);

        g.translate(offX, offY);
    
        switch (gameState)
        {
            case Menu      -> menu.render(g);
            case Customise -> customise.render(g);
            case Rules     -> rules.render(g);
            case Settings  -> settings.render(g);
            case Game      -> hud.render(g);
            case Win       -> win.render(g);
        }

        mouseInput.render(g);
    
        switch (settings.get("theme"))
        {
            case "light" -> g.setColor(Color.LIGHT_GRAY);
            case "dark"  -> g.setColor(Color.GRAY);
        }

        g.setFont(font.deriveFont(15f));
        g.drawString(VERSION, 10, 10 + 10);
        if (settings.get("displayFPS").equals("true")) g.drawString("FPS: " + fps, 10, 10 + 10 + 10 + 10);

        g.drawImage(close_operations_default, WIDTH - 10 - close_operations_default.getWidth(), 10, null);

        switch (selected_close_operation)
        {
            case 0 : g.drawImage(close_operations_default, WIDTH - 10 - close_operations_default.getWidth(), 10, null); break;
            case 1 :
                switch (settings.get("theme"))
                {
                    case "light" -> g.drawImage(close_operations_close_select_light, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                    case "dark"  -> g.drawImage(close_operations_close_select_dark, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                }
                break;
            case 2 :
                switch (settings.get("theme"))
                {
                    case "light" -> g.drawImage(close_operations_minimise_select_light, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                    case "dark"  -> g.drawImage(close_operations_minimise_select_dark, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                }
                break;
        }

        g.dispose();
        bs.show();
    }
    
    /**
     * Tries to create a connection to a running Discord client
     */
    private static void initDiscord()
    {
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) ->
        {
            ready = true;
            System.out.println("Found Discord user " + user.username + "#" + user.discriminator + ".");

            updateDiscord("In menu", "Main menu");
        }).build();
        DiscordRPC.discordInitialize("733261832948678666", handlers, true);
        DiscordRPC.discordRegister("733261832948678666", "");
    }
    
    /**
     * Updates the rich presence shown if a running Discord client is present
     *
     * @param details First line of rich presence
     * @param state Second line of rich presence
     */
    public static void updateDiscord(String details, String state)
    {
        DiscordRichPresence.Builder presence = new DiscordRichPresence.Builder(state);
        presence.setDetails(details);

        presence.setBigImage("icon", "");
        presence.setStartTimestamps(firstTick);

        DiscordRPC.discordUpdatePresence(presence.build());
    }
    
    /**
     * Main method. Starts the application and creates a new Game object
     *
     * @param args Arguments
     */
    public static void main(String[] args)
    {
        new Game();
    }
}