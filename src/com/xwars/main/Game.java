package com.xwars.main;

import com.xwars.enums.CloseOperation;
import com.xwars.enums.MessageMode;
import com.xwars.enums.OnlineMode;
import com.xwars.enums.State;
import com.xwars.gameobjects.Tile;
import com.xwars.main.input.KeyInput;
import com.xwars.main.input.MouseInput;
import com.xwars.main.loaders.ResourceLoader;
import com.xwars.online.Client;
import com.xwars.online.Message;
import com.xwars.online.Server;
import com.xwars.states.Menu;
import com.xwars.states.*;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Contains the main method along with other core elements
 * of the application, like the game loop and instance management.
 *
 * @author Soni
 * @version beta-0.2.3.1
 */
public class Game extends Canvas implements Runnable
{
    // Product info
    public static final String BRAND = "Redsea Productions";
    public static final String PRODUCT = "The Great X Wars";
    public static final String VERSION = "beta-0.2.3.1";
    
    public static int WIDTH, HEIGHT;
    public static final long firstTick = System.currentTimeMillis();

    public static ResourceBundle BUNDLE;

    private final Handler handler;
    private Thread thread;
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private boolean running = false;
    public static boolean PAUSED = false;
    public static boolean ready = false;

    public CloseOperation selected_close_operation;
    private String fps;

    // Resources
    public final ResourceLoader resourceLoader;
    
    public final BufferedImage icon;
    public final BufferedImage redsea;
    public final BufferedImage dice;
    public final BufferedImage pencil;

    public final BufferedImage close_operations_default;
    public final BufferedImage close_operations_close_select_dark;
    public final BufferedImage close_operations_close_select_light;
    public final BufferedImage close_operations_minimise_select_dark;
    public final BufferedImage close_operations_minimise_select_light;

    public final BufferedImage arrows_dark;
    public final BufferedImage arrows_light;

    public final BufferedImage arrow_left;
    public final BufferedImage arrow_right;

    private final MouseInput mouseInput;
    private final HUD hud;
    private final Settings settings;
    private final Menu menu;
    private final Customise customise;
    private final Rules rules;
    private final Win win;

    public State gameState = State.Menu;

    public Server server;
    public final Client client;
    public static final int PORT = 14242;

    public static Font font;

    public final Window window;
    
    /**
     * Constructor used in the main method to initialise the application.
     * The contents of this constructor will be executed upon application launch.
     */
    public Game()
    {
        // Setup logger
        LogManager.getLogManager().reset();
        LOGGER.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        LOGGER.addHandler(consoleHandler);
        LOGGER.info("Initialised logger");
        
        resourceLoader = new ResourceLoader();
        
        // Load Fonts
        font = resourceLoader.loadFont("fonts/font.ttf");
        LOGGER.fine("Loaded fonts");

        // Show loading window
        Window.showLoading();
        LOGGER.info("Opening loading window");

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

        LOGGER.fine("Loaded images");
        LOGGER.info("All resources loaded");

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                DiscordRPC.discordShutdown();
                LOGGER.fine("Discord hook shut down");

                settings.save();
                LOGGER.fine("Saved settings");

                server.stopServer();
                LOGGER.fine("Stopped online server");
            }
        });
        
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

        LOGGER.fine("Initialised objects");

        // Add input listeners
        this.addKeyListener(new KeyInput(this, customise));
        this.addMouseListener(mouseInput);
        this.addMouseMotionListener(mouseInput);
        this.addMouseWheelListener(mouseInput);

        LOGGER.fine("Configured input listeners");

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
                default ->
                {
                    WIDTH = 1280;
                    LOGGER.warning("Could not load resolution.\nUsing default resolution at 1280x720.");
                }
            }
            HEIGHT = WIDTH / 16 * 9;

            BUNDLE = resourceLoader.loadBundle("lang.lang_" + settings.get("language"));
            LOGGER.fine("Loaded language files");
        }
        catch (Exception e)
        {
            LOGGER.severe("Settings could not be read.");
            
            settings.reset();
            LOGGER.info("Reset settings to avoid this issue in the future.");

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
                LOGGER.warning("Discord connection failed: Timed out");
                break;
            }
        }

        // Start game
        LOGGER.info("Ready for launch.");
        LOGGER.info("Starting in resolution " + WIDTH + "x" + HEIGHT);
        window = new Window(WIDTH, HEIGHT, "The Great X Wars", this);
    }
    
    /**
     * Starts the application by creating and starting a thread
     */
    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
        LOGGER.info("Thread started");
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
            LOGGER.info("Thread stopped");
        }
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, "Could not shut down thread", e);
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
            if (running) render();
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
            if (customise.onlineMode == OnlineMode.Server)
            {
                int[] foundationPos = createFoundations();
    
                try
                {
                    if (server.connectionActive)
                    {
                        Message message = new Message();
                        message.mode = MessageMode.Start;
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

        if (selected_close_operation != null)
        {
            switch (selected_close_operation)
            {
                case Close:
                    switch (settings.get("theme"))
                    {
                        case "light" -> g.drawImage(close_operations_close_select_light, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                        case "dark"  -> g.drawImage(close_operations_close_select_dark, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                    }
                    break;
                case Minimise:
                    switch (settings.get("theme"))
                    {
                        case "light" -> g.drawImage(close_operations_minimise_select_light, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                        case "dark"  -> g.drawImage(close_operations_minimise_select_dark, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                    }
                    break;
            }
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
            LOGGER.info("Found Discord user " + user.username + "#" + user.discriminator + ".");

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