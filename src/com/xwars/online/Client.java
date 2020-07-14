package com.xwars.online;

/*
 * Author: soni801
 */

import com.xwars.main.Game;

import java.io.*;
import java.net.Socket;

public class Client
{
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public String input;
    public boolean connectionActive = false;

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
            if (!input.equals("")) System.out.println("Message from server: " + input);
        }
        catch (Exception ignored) {}
    }
}