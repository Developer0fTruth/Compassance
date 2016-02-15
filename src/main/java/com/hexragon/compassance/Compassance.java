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
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
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

    public static Economy economy;

    public void onEnable()
    {
        instance = this;

        // METRICS
        try
        {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        }
        catch (IOException e)
        {
            Misc.logHandle(Level.WARNING, "Unable to submit stats to Metrics.");
        }

        // LOAD CONFIGURATIONS
        mainConfig = new MainConfig();
        mainConfig.load();

        themeConfig = new ThemeConfig();
        themeConfig.load();

        playerConfig = new PlayerConfig();
        playerConfig.load();

        // CHECK FOR VAULT
        if (getServer().getPluginManager().getPlugin("Vault") != null)
        {
            if (setupEconomy())
            {
                Misc.logHandle(Level.INFO, "Successfully hooked into Vault economy found.");
            }
            else
            {
                Misc.logHandle(Level.WARNING, "Vault economy was not found.");
            }
        }
        else
        {
            Misc.logHandle(Level.INFO, "Vault dependency was not found.");
        }

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
        new TestCommand();

        new PlayerJoinListener();
        new PlayerQuitListener();

    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null)
        {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    public void onDisable()
    {
        playerConfig.save();
        compassTaskManager.endTaskAll();
        instance = null;
    }
}
