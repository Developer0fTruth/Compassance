package com.hexragon.compassance;

import com.hexragon.compassance.commands.CompassCommand;
import com.hexragon.compassance.commands.ReloadCommand;
import com.hexragon.compassance.commands.TestCommand;
import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.files.configs.PlayerConfig;
import com.hexragon.compassance.files.configs.ThemeConfig;
import com.hexragon.compassance.files.text.ReferenceText;
import com.hexragon.compassance.gui.MainMenu;
import com.hexragon.compassance.gui.SettingsMenu;
import com.hexragon.compassance.gui.ThemeMenu;
import com.hexragon.compassance.listeners.PlayerJoinListener;
import com.hexragon.compassance.listeners.PlayerQuitListener;
import com.hexragon.compassance.managers.compass.tasks.CompassTaskManager;
import com.hexragon.compassance.managers.compass.tasks.tracking.TrackingManager;
import com.hexragon.compassance.managers.themes.ThemeManager;
import com.hexragon.compassance.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.IOException;
import java.util.logging.Level;

public class Compassance extends JavaPlugin
{
    public static Compassance instance;

    public static ThemeConfig themeConfig;
    public static PlayerConfig playerConfig;
    public static MainConfig mainConfig;

    public static CompassTaskManager compassTaskManager;
    public static ThemeManager themeManager;
    public static TrackingManager trackingManager;

    public static MainMenu mainMenu;
    public static SettingsMenu settingsMenu;
    public static ThemeMenu themeMenu;

    public static boolean placeholderAPI;

    public void onEnable()
    {
        instance = this;

        // LOAD CONFIGURATIONS
        mainConfig = new MainConfig();
        mainConfig.load();

        themeConfig = new ThemeConfig();
        themeConfig.load();

        playerConfig = new PlayerConfig();
        playerConfig.load();

        // MANAGERS INSTANTIATION
        themeManager = new ThemeManager();
        compassTaskManager = new CompassTaskManager();
        trackingManager = new TrackingManager();

        mainMenu = new MainMenu();
        settingsMenu = new SettingsMenu();
        themeMenu = new ThemeMenu();

        themeManager.loadThemes();
        compassTaskManager.newTaskAll();

        // SECONDARY INSTANTIATION
        new ReferenceText().forceCopy();
        new CompassCommand();
        new ReloadCommand();


        if (mainConfig.config.getBoolean(MainConfig.DEBUG_MODE))
        {
            new TestCommand();

            if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            {
                placeholderAPI = true;
                Misc.logHandle(Level.INFO, "Detected PlaceholderAPI.");
                new CompassancePlaceholderHook(this);
            }
            else
            {
                placeholderAPI = false;
                Misc.logHandle(Level.INFO, "Did not find PlaceholderAPI.");
            }
        }

        // METRICS
        if (mainConfig.config.getBoolean(MainConfig.METRICS))
        {
            try
            {
                MetricsLite metrics = new MetricsLite(this);
                metrics.start();
                Misc.logHandle(Level.INFO, "Metrics initialized. You can disable this by going into the main config.");
            }
            catch (IOException e)
            {
                Misc.logHandle(Level.WARNING, "Unable to submit stats to Metrics.");
            }
        }
        else
        {
            Misc.logHandle(Level.INFO, "Metrics is disabled.");
        }

        new PlayerJoinListener();
        new PlayerQuitListener();
    }


    public void onDisable()
    {
        playerConfig.save();
        compassTaskManager.endTaskAll();
        instance = null;
    }
}
