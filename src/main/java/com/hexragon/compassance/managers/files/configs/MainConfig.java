package com.hexragon.compassance.managers.files.configs;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.misc.FileUtil;
import com.hexragon.compassance.misc.Misc;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class MainConfig
{
    public static final String USE_PERMISSIONS = "use-permissions";
    public static final String USE_TRACKING = "use-tracking";

    private final String fileName;
    public FileConfiguration config;
    private File file;

    public MainConfig()
    {
        this.fileName = "config.yml";
    }

    public void load()
    {
        if (file == null)
        {
            file = new File(Compassance.instance.getDataFolder(), fileName);

            if (!file.exists())
            {
                if (file.getParentFile().mkdirs())
                {
                    Misc.logHandle(Level.INFO, String.format("Making directory for file '%s'.", fileName));
                }
                FileUtil.copyFile(Compassance.instance.getResource(fileName), file);
                Misc.logHandle(Level.INFO, String.format("Copying file '%s' from jar to disk.", fileName));
            }
        }
        config = YamlConfiguration.loadConfiguration(file);

        Reader defConfigStream = new InputStreamReader(Compassance.instance.getResource(fileName), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        config.setDefaults(defConfig);


        if (config.getDefaults().getDouble("version") >
                config.getDouble("version"))
        {
            Misc.logHandle(Level.WARNING, "Main configs is outdated, compatibility problems may occur.");
        }
        else if (config.getDefaults().getDouble("version") <
                config.getDouble("version"))
        {
            Misc.logHandle(Level.WARNING, "Main configs version is more updated than plugin version, compatibility problems may occur.");
        }
        else
        {
            Misc.logHandle(Level.INFO, "Main configs is up to date.");
        }
    }

    public void save()
    {
        if (config == null || file == null)
        {
            Misc.logHandle(Level.SEVERE, String.format("Attempted saving file '%s' but it doesn't exist.", fileName));
            return;
        }
        try
        {
            config.save(file);
        }
        catch (IOException e)
        {
            Misc.logHandle(Level.SEVERE, "Can not save plugin files.");
            e.printStackTrace();
        }
    }

}
