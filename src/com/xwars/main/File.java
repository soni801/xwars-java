package com.xwars.main;

import java.io.FileWriter;
import java.io.IOException;

/**
 * The <code>File</code> class handles saving files to the disk
 * using its <code>save()</code> method, as well as load files
 * using <code>load()</code>
 *
 * @author soni801
 */

public class File
{
    private final String os = System.getProperty("os.name");
    private String workingDirectory;
    
    public static final String BRAND = "Redsea Productions";
    public static final String PRODUCT = "The Great X Wars";
    
    public void save(String path, String content)
    {
        if (os.toLowerCase().contains("win"))
        {
            workingDirectory = System.getenv("AppData");
            System.out.println("Working directory: " + workingDirectory);
            String absolutePath = workingDirectory + "\\" + BRAND + "\\" + PRODUCT + "\\";
            
            try
            {
                FileWriter writer = new FileWriter(absolutePath + path);
                
                writer.write(content);
                
                writer.close();
                System.out.println("Saved file " + path);
            }
            catch (IOException e)
            {
                System.out.println("Failed to save file " + path);
            }
        }
        else System.out.println("Unknown operating system. Cannot save file " + path);
    }
    
    public void load(String path)
    {
        if (os.toLowerCase().contains("win"))
        {
            workingDirectory = System.getenv("AppData");
            String absolutePath = workingDirectory + "\\" + BRAND + "\\" + PRODUCT + "\\";
        }
        else System.out.println("Unknown operating system. Cannot load file from " + path);
    }
}