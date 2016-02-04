package com.hexsquared.compassance;

import com.hexsquared.compassance.commands.CompassCommand;
import com.hexsquared.compassance.commands.ReloadCommand;
import com.hexsquared.compassance.commands.TestCommand;
import com.hexsquared.compassance.gui.MainMenu;
import com.hexsquared.compassance.gui.SettingsMenu;
import com.hexsquared.compassance.gui.ThemeMenu;
import com.hexsquared.compassance.listeners.PlayerJoinListener;
import com.hexsquared.compassance.listeners.PlayerQuitListener;
import com.hexsquared.compassance.managers.compass.tasks.CompassTaskManager;
import com.hexsquared.compassance.managers.compass.tasks.tracking.TrackingManager;
import com.hexsquared.compassance.managers.settings.ConfigFileManager;
import com.hexsquared.compassance.managers.themes.ThemeManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Compassance extends JavaPlugin
{
    public static Compassance instance;

    public ConfigFileManager configManager;
    public CompassTaskManager compassTaskManager;
    public ThemeManager themeManager;
    public TrackingManager trackingManager;

    public MainMenu mainMenu;
    public SettingsMenu settingsMenu;
    public ThemeMenu themeMenu;

    public void onEnable()
    {
        instance = this;

        configManager = new ConfigFileManager();
        configManager.loadThemeConfig();
        configManager.loadPlayerConfig();

        themeManager = new ThemeManager();
        compassTaskManager = new CompassTaskManager();
        trackingManager = new TrackingManager();

        mainMenu = new MainMenu();
        settingsMenu = new SettingsMenu();
        themeMenu = new ThemeMenu();

        themeManager.loadThemes();
        compassTaskManager.newTaskAll();

        new CompassCommand();
        new ReloadCommand();
        new TestCommand();

        new PlayerJoinListener();
        new PlayerQuitListener();


    }

    public void onDisable()
    {
        configManager.savePlayerSettings();

        instance = null;
    }
}
