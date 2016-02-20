package com.hexragon.compassance;

import com.hexragon.compassance.commands.CompassCommand;
import com.hexragon.compassance.commands.ReloadCommand;
import com.hexragon.compassance.commands.TestCommand;
import com.hexragon.compassance.files.Gearbox;
import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.files.text.GearboxText;
import com.hexragon.compassance.gui.MainMenu;
import com.hexragon.compassance.gui.SettingsMenu;
import com.hexragon.compassance.gui.ThemeMenu;
import com.hexragon.compassance.managers.compass.tasks.TaskManager;
import com.hexragon.compassance.managers.compass.tasks.tracking.TrackingManager;
import com.hexragon.compassance.managers.themes.ThemeManager;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin
{
    public static Main instance;

    public static Gearbox themeConfig;
    public static Gearbox playerConfig;
    public static Gearbox mainConfig;

    public static TaskManager taskManager;
    public static ThemeManager themeManager;
    public static TrackingManager trackingManager;

    public static MainMenu mainMenu;
    public static SettingsMenu settingsMenu;
    public static ThemeMenu themeMenu;

    public static boolean placeholderAPIExist;

    public void onEnable()
    {
        instance = this;

        // LOAD CONFIGURATIONS
        mainConfig = new Gearbox(this, "config.yml");
        mainConfig.load();

        themeConfig = new Gearbox(this, "themes-config.yml");
        themeConfig.load();

        playerConfig = new Gearbox(this, "players-config.yml");
        playerConfig.load();

        // MANAGERS INSTANTIATION
        themeManager = new ThemeManager();
        taskManager = new TaskManager();
        trackingManager = new TrackingManager();

        mainMenu = new MainMenu();
        settingsMenu = new SettingsMenu();
        themeMenu = new ThemeMenu();

        themeManager.loadThemes();
        taskManager.newTaskAll();

        // SECONDARY INSTANTIATION
        new GearboxText(this, "references.txt").load();
        new CompassCommand();
        new ReloadCommand();

        if (mainConfig.config.getBoolean(MainConfig.DEBUG_MODE.path))
        {
            new TestCommand();

            if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            {
                placeholderAPIExist = true;
                Utils.logHandle(Level.INFO, "Detected PlaceholderAPI.");
                new Placeholders(this);
            }
            else
            {
                placeholderAPIExist = false;
                Utils.logHandle(Level.INFO, "Did not find PlaceholderAPI.");
            }
        }

        // METRICS
        if (mainConfig.config.getBoolean(MainConfig.METRICS.path))
        {
            try
            {
                MetricsLite metrics = new MetricsLite(this);
                metrics.start();
                Utils.logHandle(Level.INFO, "Metrics initialized. You can disable this by going into the meta config.");
            }
            catch (IOException e)
            {
                Utils.logHandle(Level.WARNING, "Unable to submit stats to Metrics.");
            }
        }
        else
        {
            Utils.logHandle(Level.INFO, "Metrics is disabled.");
        }

        new Listeners();
    }


    public void onDisable()
    {
        playerConfig.save();
        taskManager.endTaskAll();
        instance = null;
    }
}
