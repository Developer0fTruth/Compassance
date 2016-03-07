package com.hexragon.compassance;


import com.hexragon.compassance.commands.AdminCommand;
import com.hexragon.compassance.commands.CompassCommand;
import com.hexragon.compassance.commands.TestCommand;
import com.hexragon.compassance.files.Gearbox;
import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.gui.MainMenu;
import com.hexragon.compassance.gui.SettingsMenu;
import com.hexragon.compassance.gui.ThemeMenu;
import com.hexragon.compassance.managers.tasks.CTaskManager;
import com.hexragon.compassance.managers.tasks.tracking.TrackingManager;
import com.hexragon.compassance.managers.themes.ThemeManager;
import com.hexragon.compassance.utils.ActionBar;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends JavaPlugin
{
    public static Main plugin;
    public static Logger logger;
    public static String nmsver;

    public static Gearbox themeConfig;
    public static Gearbox playerConfig;
    public static Gearbox mainConfig;

    public static CTaskManager taskManager;
    public static ThemeManager themeManager;
    public static TrackingManager trackingManager;

    public static MainMenu mainMenu;
    public static SettingsMenu settingsMenu;
    public static ThemeMenu themeMenu;

    public static boolean placeholderAPIExist;
    public static boolean bossbarAPIExist;
    private UpdateChecker uct;

    public void onEnable()
    {
        plugin = this;
        logger = plugin.getLogger();

        String bukkitPackage = Main.plugin.getServer().getClass().getPackage().getName();
        nmsver = bukkitPackage.substring(bukkitPackage.lastIndexOf(".") + 1);

        // LOAD CONFIGURATIONS
        mainConfig = new Gearbox(this, "config.yml");
        mainConfig.load();
        Utils.gbVersionCheck(mainConfig);

        themeConfig = new Gearbox(this, "themes-config.yml");
        themeConfig.load();
        Utils.gbVersionCheck(themeConfig);

        playerConfig = new Gearbox(this, "players-config.yml");
        playerConfig.load();
        Utils.gbVersionCheck(playerConfig);

        // MANAGERS INSTANTIATION
        themeManager = new ThemeManager();
        taskManager = new CTaskManager();
        trackingManager = new TrackingManager();

        mainMenu = new MainMenu();
        settingsMenu = new SettingsMenu();
        themeMenu = new ThemeMenu();

        themeManager.loadThemes();

        // SECONDARY INSTANTIATION
        ActionBar.setup();
        new CompassCommand();
        new AdminCommand();

        // PLACEHOLDER API DEPENDENCY
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
        {
            placeholderAPIExist = true;
            getLogger().info("Detected PlaceholderAPI. Placeholder functionality will work.");
            new Placeholders(this);
        }
        else
        {
            placeholderAPIExist = false;
            getLogger().warning("Did not find PlaceholderAPI. Placeholder functionality will not work.");
        }

        if (mainConfig.config.getBoolean(MainConfig.DEBUG_MODE.path))
        {
            new TestCommand();

            if (getServer().getPluginManager().getPlugin("BossBarAPI") != null && Bukkit.getPluginManager().isPluginEnabled("BossBarAPI"))
            {
                bossbarAPIExist = true;
                getLogger().info("Detected BossBarAPI.");
            }
            else
            {
                bossbarAPIExist = false;
                getLogger().info("Did not find BossBarAPI.");
            }
        }

        // METRICS
        if (mainConfig.config.getBoolean(MainConfig.METRICS.path))
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

        if (mainConfig.config.getBoolean(MainConfig.UPDATE_CHECK.path))
        {
            uct = new UpdateChecker();
            uct.start();
        }

        new Listeners();

        taskManager.newTaskAll();
    }

    public void onDisable()
    {
        playerConfig.save();
        taskManager.endTaskAll();
        uct.stop();

        plugin = null;
    }
}
