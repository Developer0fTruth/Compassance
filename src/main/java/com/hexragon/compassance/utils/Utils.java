package com.hexragon.compassance.utils;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.configs.ConfigurationPaths;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils
{
    public static String fmtClr(String s)
    {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static boolean permHandle(Player p, String perm, boolean ifPermIsNull)
    {
        if (perm == null)
        {
            return ifPermIsNull;
        }
        else
        {
            return p.hasPermission(perm);
        }
    }

    public static void updateProfile(Player p)
    {
        Main.playerConfig.config.set(ConfigurationPaths.PlayerConfig.PLAYER_NAME.format(p.getPlayer().getUniqueId().toString()), p.getName());

        String selectedTheme = Main.playerConfig.config.getString(ConfigurationPaths.PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString()));

        if (selectedTheme == null || Main.themeManager.getTheme(selectedTheme) == null)
        {
            Main.playerConfig.config
                    .set(ConfigurationPaths.PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString()), Main.themeManager.getDefaultID());

            Main.playerConfig.config
                    .set(ConfigurationPaths.PlayerConfig.SETTING_ENABLE.format(p.getPlayer().getUniqueId().toString()),
                            Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_ENABLE.format("default")));
            Main.playerConfig.config
                    .set(ConfigurationPaths.PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()),
                            Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_CURSOR.format("default")));
            Main.playerConfig.config
                    .set(ConfigurationPaths.PlayerConfig.SETTING_ALWAYSON.format(p.getPlayer().getUniqueId().toString()),
                            Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_ALWAYSON.format("default")));
            Main.playerConfig.config
                    .set(ConfigurationPaths.PlayerConfig.SETTING_TRACKING.format(p.getPlayer().getUniqueId().toString()),
                            Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_TRACKING.format("default")));
        }
    }
}
