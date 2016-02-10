package com.hexragon.compassance;

import com.hexragon.compassance.commands.CompassCommand;
import com.hexragon.compassance.commands.ReloadCommand;
import com.hexragon.compassance.commands.TestCommand;
import com.hexragon.compassance.gui.MainMenu;
import com.hexragon.compassance.gui.SettingsMenu;
import com.hexragon.compassance.gui.ThemeMenu;
import com.hexragon.compassance.listeners.PlayerJoinListener;
import com.hexragon.compassance.listeners.PlayerQuitListener;
import com.hexragon.compassance.managers.compass.tasks.CompassTaskManager;
import com.hexragon.compassance.managers.compass.tasks.tracking.TrackingManager;
import com.hexragon.compassance.managers.settings.ConfigFileManager;
import com.hexragon.compassance.managers.themes.ThemeManager;
import com.hexragon.compassance.misc.Misc;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.IOException;
import java.util.logging.Level;

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

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            Misc.logHandle(Level.WARNING, "Unable to submit stats to Metrics.");
        }

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
        compassTaskManager.endTaskAll();
        instance = null;
    }
}
