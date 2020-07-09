package com.xwars.online;

/*
 * Author: soni801
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class Server
{
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    String ip;
    public String status;

    public Server()
    {
        try
        {
            // Get public IP
            URL ipCheck = new URL("http://checkip.amazonaws.com");
            BufferedReader reader = new BufferedReader(new InputStreamReader(ipCheck.openStream()));
            ip = reader.readLine();

            // Start server
            status = "Starting server...";
            System.out.println(status);
            serverSocket = new ServerSocket(7777);
            System.out.println("Server IP: " + ip);
            status = "Waiting for connection...";
            System.out.println(status);
            socket = serverSocket.accept();
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println("Failed to start server");
        }
    }

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
}