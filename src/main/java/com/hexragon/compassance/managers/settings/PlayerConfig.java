package com.hexragon.compassance.managers.settings;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.misc.FileUtil;
import com.hexragon.compassance.misc.Misc;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class PlayerConfig
{
    public static final String PLAYER_NAME = "players.%s.name";

    public static final String SETTING_SELECTEDTHEME = "players.%s.settings.selectedTheme";
    public static final String SETTING_ENABLE = "players.%s.settings.enableCompass";
    public static final String SETTING_CURSOR = "players.%s.settings.enableCursor";
    public static final String SETTING_ALWAYSON = "players.%s.settings.alwaysOn";
    public static final String SETTING_TRACKING = "players.%s.settings.tracking";

    private final String fileName;
    public FileConfiguration config;
    private File file;

    public PlayerConfig()
    {
        this.fileName = "players-config.yml";
    }

    /**
     * Update the player name in the player config.
     * Create a new profile for the player-uuid if
     * selected theme is not found.
     *
     * @param p Player
     */
    public static void updateProfile(Player p)
    {
        Compassance.instance.playerConfig.config.set(String.format(PlayerConfig.PLAYER_NAME, p.getPlayer().getUniqueId().toString()), p.getName());

        String selectedTheme = Compassance.instance.playerConfig.config.getString(String.format(PlayerConfig.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()));
        if (selectedTheme == null || Compassance.instance.themeManager.getTheme(selectedTheme) == null)
        {
            Compassance.instance.playerConfig.config
                    .set(String.format(PlayerConfig.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()), Compassance.instance.themeManager.getDefaultID());

            Compassance.instance.playerConfig.config
                    .set(String.format(PlayerConfig.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()),
                            Compassance.instance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_ENABLE, "default")));
            Compassance.instance.playerConfig.config
                    .set(String.format(PlayerConfig.SETTING_CURSOR, p.getPlayer().getUniqueId().toString()),
                            Compassance.instance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_CURSOR, "default")));
            Compassance.instance.playerConfig.config
                    .set(String.format(PlayerConfig.SETTING_ALWAYSON, p.getPlayer().getUniqueId().toString()),
                            Compassance.instance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_ALWAYSON, "default")));
            Compassance.instance.playerConfig.config
                    .set(String.format(PlayerConfig.SETTING_TRACKING, p.getPlayer().getUniqueId().toString()),
                            Compassance.instance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_TRACKING, "default")));
        }
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
            Misc.logHandle(Level.WARNING, "PLayer config is outdated, compatibility problems may occur.");
        }
        else if (config.getDefaults().getDouble("version") <
                config.getDouble("version"))
        {
            Misc.logHandle(Level.WARNING, "PLayer config version is more updated than plugin version, compatibility problems may occur.");
        }
        else
        {
            Misc.logHandle(Level.INFO, "Player config is up to date.");
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
            Misc.logHandle(Level.SEVERE, "Can not save player settings.");
            e.printStackTrace();
        }
    }
}
