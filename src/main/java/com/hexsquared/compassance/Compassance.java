package com.hexsquared.compassance;

import com.hexsquared.compassance.commands.ReloadCommand;
import com.hexsquared.compassance.commands.TestCommand;
import com.hexsquared.compassance.listeners.PlayerJoinListener;
import com.hexsquared.compassance.listeners.PlayerQuitListener;
import com.hexsquared.compassance.managers.compass.CompassTaskManager;
import com.hexsquared.compassance.managers.settings.ConfigFileManager;
import com.hexsquared.compassance.managers.themes.ThemeManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Compassance extends JavaPlugin
{
    private static Compassance instance;
    private static ConfigFileManager configManager;
    private static CompassTaskManager compassTaskManager;
    private static ThemeManager themeManager;

    public static Compassance getInstance()
    {
        return instance;
    }

    public static ConfigFileManager getConfigManager()
    {
        return configManager;
    }

    public static CompassTaskManager getCompassTaskManager()
    {
        return compassTaskManager;
    }

    public static ThemeManager getThemeManager()
    {
        return themeManager;
    }


    public void onEnable()
    {
        instance = this;
        configManager = new ConfigFileManager();
        themeManager = new ThemeManager();
        compassTaskManager = new CompassTaskManager();

        themeManager.loadThemes();
        compassTaskManager.newTaskAll();

        new ReloadCommand();
        new TestCommand();

        new PlayerJoinListener();
        new PlayerQuitListener();
    }


    public void onDisable()
    {
        configManager.savePlayerSettings();

        instance = null;
        configManager = null;
    }

}
