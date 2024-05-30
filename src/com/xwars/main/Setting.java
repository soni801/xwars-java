package com.xwars.main;

import com.xwars.enums.SettingDisplayMode;

import java.io.Serializable;

/**
 * Used to hold information about a setting in the application
 *
 * @author Soni
 * @version 1
 */
public class Setting implements Serializable
{
    public final String name;
    public final String[] values;

    /**
     * How the setting is displayed
     */
    public final SettingDisplayMode displayMode;
    
    /**
     * Constructor
     *
     * @param name Name of the setting
     * @param values The different values the setting can have
     * @param displayMode How the setting is displayed
     */
    public Setting(String name, String[] values, SettingDisplayMode displayMode)
    {
        this.name = name;
        this.values = values;
        this.displayMode = displayMode;
    }
}