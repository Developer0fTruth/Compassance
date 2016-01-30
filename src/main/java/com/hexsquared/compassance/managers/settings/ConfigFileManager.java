package com.hexsquared.compassance.managers.settings;

import com.hexsquared.compassance.misc.FileUtil;
import com.hexsquared.compassance.misc.Misc;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import static com.hexsquared.compassance.Compassance.getInstance;

public class ConfigFileManager
{
    // Files
    private File playerSettingsFile;
    public FileConfiguration playerSettings;
    private final String playerSettingsFileName;

    private File themeConfigFile;
    public FileConfiguration themeConfig;
    private final String themeConfigFileName;

    public ConfigFileManager()
    {
        this.themeConfigFileName = "themes-config.yml";
        this.playerSettingsFileName = "players-config.yml";

        loadThemeConfig();
        loadPlayerConfig();
    }

    public FileConfiguration getPlayerSettings()
    {
        if(playerSettings == null)
        {
            loadPlayerConfig();
        }
        return playerSettings;
    }

    public FileConfiguration getThemeConfig()
    {
        if(themeConfig == null)
        {
            loadThemeConfig();
        }
        return themeConfig;
    }

    public void loadPlayerConfig()
    {

        if (playerSettingsFile == null)
        {
            playerSettingsFile = new File(getInstance().getDataFolder(), playerSettingsFileName);

            if (!playerSettingsFile.exists())
            {
                if (playerSettingsFile.getParentFile().mkdirs())
                {
                    Misc.logHandle(Level.INFO, String.format("Making directory for file '%s'.", playerSettingsFileName));
                }
                FileUtil.copyFile(getInstance().getResource(playerSettingsFileName), playerSettingsFile);
                Misc.logHandle(Level.INFO, String.format("Copying file '%s' from jar to disk.", playerSettingsFileName));
            }
        }
        playerSettings = YamlConfiguration.loadConfiguration(playerSettingsFile);

        Reader defConfigStream = new InputStreamReader(getInstance().getResource(playerSettingsFileName), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        playerSettings.setDefaults(defConfig);

    }

    public void loadThemeConfig()
    {
        if (themeConfigFile == null)
        {
            themeConfigFile = new File(getInstance().getDataFolder(), themeConfigFileName);

            if (!themeConfigFile.exists())
            {
                if (themeConfigFile.getParentFile().mkdirs())
                {
                    Misc.logHandle(Level.INFO, String.format("Making directory for file '%s'.",themeConfigFileName));
                }
                FileUtil.copyFile(getInstance().getResource(themeConfigFileName), themeConfigFile);
                Misc.logHandle(Level.INFO, String.format("Copying file '%s' from jar to disk.", themeConfigFileName));
            }
        }
        themeConfig = YamlConfiguration.loadConfiguration(themeConfigFile);

        Reader defConfigStream = new InputStreamReader(getInstance().getResource(themeConfigFileName), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        themeConfig.setDefaults(defConfig);
    }

    public void savePlayerSettings() 
    {
        if(playerSettings == null || playerSettingsFile == null)
        {
            Misc.logHandle(Level.SEVERE, String.format("Attempted saving file '%s' but it doesn't exist.", playerSettingsFileName));
            return;
        }
        try
        {
            getPlayerSettings().save(playerSettingsFile);
        }
        catch (IOException e)
        {
            Misc.logHandle(Level.SEVERE, "Can not save player settings.");
            e.printStackTrace();
        }
    }

    public void saveThemeSettings()
    {
        if(themeConfig == null || themeConfigFile == null)
        {
            Misc.logHandle(Level.SEVERE, String.format("Attempted saving file '%s' but it doesn't exist.", themeConfigFileName));
            return;
        }
        try 
        {
            getThemeConfig().save(themeConfigFile);

        }
        catch (IOException e)
        {
            Misc.logHandle(Level.SEVERE, "Can not save plugin settings.");
            e.printStackTrace();
        }
    }

}
