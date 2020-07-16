package com.xwars.main;

import com.xwars.main.input.*;
import com.xwars.online.*;
import com.xwars.states.*;
import com.xwars.states.Menu;
import net.arikia.dev.drpc.*;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.*;

/**
 * The main class of the application.
 *
 * @author soni801
 */

public class Game extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L;

    public static int WIDTH, HEIGHT;
    public static final String VERSION = "alpha-0.0.7.1";
    public static long firstTick = System.currentTimeMillis();

    public static ResourceBundle BUNDLE;

    private final Handler handler;
    private Thread thread;

    private boolean running = false;
    public static boolean PAUSED = false;
    public static boolean ready = false;

    public int selected_close_operation;

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

    public State gameState = State.Menu;

    public Server server;
    public Client client;
    public static int PORT = 14242;

    public static Font font;

    public Window window;

    public Game()
    {
        try
        {
            font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("fonts/font.ttf")));
        }
        catch (IOException|FontFormatException e)
        {
            e.printStackTrace();
        }

        Window.showLoading();

        BufferedImageLoader loader = new BufferedImageLoader();
        icon = loader.loadImage("/images/icon.png");
        redsea = loader.loadImage("/images/redsea.png");
        dice = loader.loadImage("/images/dice.png");
        pencil = loader.loadImage("/images/pencil.png");

        close_operations_default = loader.loadImage("/images/close_operations/default.png");
        close_operations_close_select_dark = loader.loadImage("/images/close_operations/close_select_dark.png");
        close_operations_close_select_light = loader.loadImage("/images/close_operations/close_select_light.png");
        close_operations_minimise_select_dark = loader.loadImage("/images/close_operations/minimise_select_dark.png");
        close_operations_minimise_select_light = loader.loadImage("/images/close_operations/minimise_select_light.png");

        arrows_dark = loader.loadImage("/images/arrows_dark.png");
        arrows_light = loader.loadImage("/images/arrows_light.png");

        arrow_left = loader.loadImage("/images/settings/arrow_left.png");
        arrow_right = loader.loadImage("/images/settings/arrow_right.png");

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            public void run()
            {
                System.out.println("Closing Discord hook...");
                DiscordRPC.discordShutdown();
                settings.save();
                server.stopServer();
            }
        }, "Shutdown-thread"));

        handler = new Handler();

        customise = new Customise(this);
        hud = new HUD(handler, customise);
        settings = new Settings(this);
        menu = new Menu(this);

        server = new Server(customise, hud, handler);
        client = new Client(this, customise, hud, handler);

        mouseInput = new MouseInput(handler, hud, this, customise);

        this.addKeyListener(new KeyInput(this, customise));
        this.addMouseListener(mouseInput);
        this.addMouseMotionListener(mouseInput);

        settings.load();

        switch (Settings.settings.get("resolution"))
        {
            case "960x540"  : WIDTH = 960;  break;
            case "1280x720" : WIDTH = 1280; break;
            case "1600x900" : WIDTH = 1600; break;
            default :
                WIDTH = 1280;
                System.out.println("Could not load resolution correctly. Using default resolution at 1280x720.");
        }
        HEIGHT = WIDTH / 16 * 9;

        BUNDLE = ResourceBundle.getBundle("lang.lang_" + Settings.settings.get("language"));

        initDiscord();
        while (!ready) DiscordRPC.discordRunCallbacks();

        System.out.println("Starting in resolution " + WIDTH + "x" + HEIGHT);
        window = new Window(WIDTH, HEIGHT, "The Great X Wars", this);
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
                if (Settings.settings.get("printfps").equals("true")) System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    public void startGame()
    {
        gameState = State.Game;
        hud.generate(customise.boardSize[0], customise.boardSize[1]);
    }

    private void tick()
    {
        switch (gameState)
        {
            case Menu      : menu.tick();      break;
            case Customise : customise.tick(); break;
            case Settings  : settings.tick();  break;
            case Game      : hud.tick();       break;
        }

        handler.tick();
    }

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

        switch (Settings.settings.get("theme"))
        {
            case "light" : g.setColor(Color.WHITE);     break;
            case "dark"  : g.setColor(Color.DARK_GRAY); break;
        }

        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.translate(-offX, -offY);

        handler.render(g);

        g.translate(offX, offY);

        switch (gameState)
        {
            case Menu      : menu.render(g);      break;
            case Customise : customise.render(g); break;
            case Settings  : settings.render(g);  break;
            case Game      : hud.render(g);       break;
        }

        mouseInput.render(g);

        switch (Settings.settings.get("theme"))
        {
            case "light" : g.setColor(Color.LIGHT_GRAY); break;
            case "dark"  : g.setColor(Color.GRAY);       break;
        }

        g.setFont(font.deriveFont(15f));
        g.drawString(VERSION, 10, 10 + 10);

        g.drawImage(close_operations_default, WIDTH - 10 - close_operations_default.getWidth(), 10, null);

        switch (selected_close_operation)
        {
            case 0 : g.drawImage(close_operations_default, WIDTH - 10 - close_operations_default.getWidth(), 10, null); break;
            case 1 :
                switch (Settings.settings.get("theme"))
                {
                    case "light" :
                        g.drawImage(close_operations_close_select_light, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                        break;
                    case "dark" :
                        g.drawImage(close_operations_close_select_dark, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                        break;
                }
                break;
            case 2 :
                switch (Settings.settings.get("theme"))
                {
                    case "light" :
                        g.drawImage(close_operations_minimise_select_light, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                        break;
                    case "dark" :
                        g.drawImage(close_operations_minimise_select_dark, WIDTH - 10 - close_operations_default.getWidth(), 10, null);
                        break;
                }
                break;
        }

        g.dispose();
        bs.show();
    }

    private static void initDiscord()
    {
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) ->
        {
            ready = true;
            System.out.println("Welcome " + user.username + "#" + user.discriminator + ".");

            updateDiscord("In menu", "Main menu");
        }).build();
        DiscordRPC.discordInitialize("733261832948678666", handlers, false);
        DiscordRPC.discordRegister("733261832948678666", "");
    }

    public static void updateDiscord(String details, String state)
    {
        DiscordRichPresence.Builder presence = new DiscordRichPresence.Builder(state);
        presence.setDetails(details);

        presence.setBigImage("icon", "");
        presence.setStartTimestamps(firstTick);

        DiscordRPC.discordUpdatePresence(presence.build());
    }

    public static void main(String[] args)
    {
        new Game();
    }
}