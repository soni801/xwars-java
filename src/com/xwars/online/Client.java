package com.xwars.online;

/*
 * Author: soni801
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client
{
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public void connect(String ip)
    {
        try
        {
            System.out.println("Starting client...");
            socket = new Socket(ip, 7777);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected to server " + ip);
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
}