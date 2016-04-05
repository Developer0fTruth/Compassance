package com.hexragon.compassance;

import com.hexragon.compassance.commands.AdminCommand;
import com.hexragon.compassance.commands.CompassCommand;
import com.hexragon.compassance.configs.ConfigurationManager;
import com.hexragon.compassance.configs.ConfigurationPaths;
import com.hexragon.compassance.gui.MainMenu;
import com.hexragon.compassance.gui.SettingsMenu;
import com.hexragon.compassance.gui.ThemeMenu;
import com.hexragon.compassance.managers.tasks.CompassTaskManager;
import com.hexragon.compassance.managers.themes.ThemeManager;
import com.hexragon.compassance.managers.tracking.TrackingManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends JavaPlugin
{
    public static Main plugin;
    public static Logger logger;

    public static ConfigurationManager themeConfig;
    public static ConfigurationManager playerConfig;
    public static ConfigurationManager mainConfig;

    public static CompassTaskManager taskManager;
    public static ThemeManager themeManager;
    public static TrackingManager trackingManager;

    public static MainMenu mainMenu;
    public static SettingsMenu settingsMenu;
    public static ThemeMenu themeMenu;

    public static boolean placeholderAPIExist;
    private UpdateChecker updateChecker;

    public void onEnable()
    {
        plugin = this;
        logger = plugin.getLogger();

        // LOAD CONFIGURATIONS
        mainConfig = new ConfigurationManager(this, "config.yml");
        mainConfig.load();

        themeConfig = new ConfigurationManager(this, "themes-config.yml");
        themeConfig.load();

        playerConfig = new ConfigurationManager(this, "players-config.yml");
        playerConfig.load();

        // MANAGERS INSTANTIATION
        themeManager = new ThemeManager();
        taskManager = new CompassTaskManager();
        trackingManager = new TrackingManager();

        mainMenu = new MainMenu();
        settingsMenu = new SettingsMenu();
        themeMenu = new ThemeMenu();

        themeManager.loadThemes();

        // SECONDARY INSTANTIATION
        new CompassCommand();
        new AdminCommand();

        // PLACEHOLDER API DEPENDENCY
        placeholderAPIExist = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        if (placeholderAPIExist)
        {
            getLogger().info("Detected PlaceholderAPI. Placeholder functionality will work.");
            new Placeholders(this);
        }
        else
        {
            getLogger().warning("Did not find PlaceholderAPI. Placeholder functionality will not work.");
        }

        // METRICS
        if (mainConfig.config.getBoolean(ConfigurationPaths.MainConfig.METRICS.path))
        {
            try
            {
                MetricsLite metrics = new MetricsLite(this);
                metrics.start();
                getLogger().info("Metrics initialized. You can disable this by going into the main config.");
            }
            catch (IOException e)
            {
                getLogger().warning("Unable to submit data to Metrics.");
            }
        }
        else
        {
            getLogger().info("Metrics is disabled.");
        }

        if (mainConfig.config.getBoolean(ConfigurationPaths.MainConfig.UPDATE_CHECK.path))
        {
            updateChecker = new UpdateChecker();
            updateChecker.start();
        }

        new Listeners();

        taskManager.newTaskAll();
    }

    public void onDisable()
    {
        playerConfig.save();
        taskManager.endTaskAll();
        if (updateChecker != null) updateChecker.cancel();

        plugin = null;
    }
}
