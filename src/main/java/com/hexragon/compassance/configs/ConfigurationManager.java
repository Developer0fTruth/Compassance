package com.hexragon.compassance.configs;

import com.hexragon.compassance.utils.FileUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 * A solution to easy configuration loading,
 * saving, and general management.
 * <p/>
 * Please have respect for the original
 * author and give proper credits for their
 * work.
 *
 * @author Hexragon
 */
public class ConfigurationManager
{
    /**
     * File name of configuration grabbed
     * from the plugin's resource folder.
     */
    public final String fileName;
    /**
     * Main java instance required to grab the
     * plugin folder and handle writing logs.
     */
    private final JavaPlugin instance;
    /**
     * Access to the loaded configuration
     * once you used [Gearbox].load().
     */
    public FileConfiguration config;

    /**
     * The file of the Gearbox instance.
     * Don't know why you would need it
     * if you're just going to set and
     * grab some data nodes.
     */
    public File file;

    /**
     * Creates a new instance of SimpleGearbox.
     *
     * @param i Main plugin instance.
     * @param s Configuration file name.
     */
    public ConfigurationManager(JavaPlugin i, String s)
    {
        instance = i;
        fileName = s;
    }

    /**
     * Loads the config from the file name. This will
     * attempt to grab the file from the plugin folder.
     * If one is not found, the plugin will directly
     * copy from it's resources and write it into the
     * file destination in the folder.
     * <p/>
     * The default config from plugin resources is
     * also loaded.
     * <p/>
     * This also functions as a quick reload.
     */
    public void load()
    {
        if (file == null)
        {
            file = new File(instance.getDataFolder(), fileName);

            if (!file.exists())
            {
                if (file.getParentFile().mkdirs())
                {
                    instance.getLogger().log(Level.INFO, String.format("Making directory for file '%s'.", fileName));
                }
                FileUtil.write(instance.getResource(fileName), file);
                instance.getLogger().info(String.format("Copying file '%s' from jar to disk.", fileName));
            }
        }
        config = YamlConfiguration.loadConfiguration(file);

        Reader defConfigStream = new InputStreamReader(instance.getResource(fileName), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        config.setDefaults(defConfig);
    }

    /**
     * Save the configuration to the file.
     */
    public void save()
    {
        if (config == null || file == null)
        {
            instance.getLogger().severe(String.format("Attempted saving file '%s' but it doesn't exist.", fileName));
            return;
        }
        try
        {
            config.save(file);
        }
        catch (IOException e)
        {
            instance.getLogger().severe(String.format("Attempted saving file '%s' encountered an IOException.", fileName));
            e.printStackTrace();
        }
    }
}
