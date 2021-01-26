package com.xwars.main;

import java.io.Serializable;

/**
 * The <code>Setting</code> class is used to hold information
 * about a single setting in the application
 *
 * @author soni801
 */

public class Setting implements Serializable
{
    public String name;
    public String[] values;
    
    /*
     * 0 = select
     * 1 = slider
     */
    public int displayMode;
    
    public Setting(String name, String[] values, int displayMode)
    {
        this.name = name;
        this.values = values;
        this.displayMode = displayMode;
    }
}