package com.hexragon.compassance.files.configs;

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

public class ThemeConfig
{
    public static final String ENABLED_THEMES = "enabled-themes";

    public static final String THEME_META_NAME = "themes.%s.meta.name";
    public static final String THEME_META_DESC = "themes.%s.meta.desc";
    public static final String THEME_META_PERM = "themes.%s.meta.permission";

    public static final String THEME_DATA_MAIN_PATTERN_MAP = "themes.%s.data.main-pattern-map";

    public static final String THEME_DATA_FUNC_CURSOR = "themes.%s.data.function.cursor";
    public static final String THEME_DATA_FUNC_TARGET = "themes.%s.data.function.target";

    public static final String THEME_DATA_DIRECT_REPLACER = "themes.%s.data.direct.%s"; //id,replacer,id

    public static final String THEME_DATA_SUBPATTERN_MAP = "themes.%s.data.sub-pattern.%s.pattern-map"; //id,sep-id
    public static final String THEME_DATA_SUBPATTERN_MAP_REPLACER = "themes.%s.data.sub-pattern.%s.%s"; //id,sep-id,replacer-id

    public static final String THEME_FINAL_MAIN_PATTERN_MAP = "themes.%s.final.pattern-map";
    public static final String THEME_FINAL_DIRECT_REPLACER = "themes.%s.final.%s";

    private final String fileName;
    public FileConfiguration config;
    private File file;

    public ThemeConfig()
    {
        this.fileName = "themes-config.yml";
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

        if (!config.getDefaults().getString("version").equals(config.getString("version")) || config.getString("version") == null)
        {
            Misc.logHandle(Level.WARNING, "Theme config is outdated, compatibility problems may occur.");
        }
        else
        {
            Misc.logHandle(Level.INFO, "Theme config is up to date.");
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
            Misc.logHandle(Level.SEVERE, "Can not save plugin text.");
            e.printStackTrace();
        }
    }

}
