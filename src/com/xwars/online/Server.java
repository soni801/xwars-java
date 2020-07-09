package com.xwars.online;

/*
 * Author: soni801
 */

import com.xwars.main.Game;

import java.io.*;
import java.net.*;

public class Server extends Thread
{
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    private String ip;

    public String status;

    public String getIp() { return ip; }

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
            System.out.println(status);
            socket = serverSocket.accept();
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            status = "Failed to start server";
            System.out.println(status);
        }
    }
}