package com.hexragon.compassance.utils;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.files.configs.PlayerConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Utils
{

    public static String fmtClr(String s)
    {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void logHandle(Level l, String msg)
    {
        String note = "|+| ";
        if (l == Level.SEVERE || l == Level.WARNING)
        {
            note = "!#! ";
        }
        Main.instance.getLogger().log(l, note + msg);
    }

    public static boolean permHandle(Player p, String perm, boolean ifPermIsNull)
    {
        return perm != null && p.hasPermission(perm) || perm == null && ifPermIsNull;
    }

    public static void updateProfile(Player p)
    {
        Main.playerConfig.config.set(PlayerConfig.PLAYER_NAME.format(p.getPlayer().getUniqueId().toString()), p.getName());

        String selectedTheme = Main.playerConfig.config.getString(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString()));
        if (selectedTheme == null || Main.themeManager.getTheme(selectedTheme) == null)
        {
            Main.playerConfig.config
                    .set(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString()), Main.themeManager.getDefaultID());

            Main.playerConfig.config
                    .set(PlayerConfig.SETTING_ENABLE.format(p.getPlayer().getUniqueId().toString()),
                            Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_ENABLE.format("default")));
            Main.playerConfig.config
                    .set(PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()),
                            Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_CURSOR.format("default")));
            Main.playerConfig.config
                    .set(PlayerConfig.SETTING_ALWAYSON.format(p.getPlayer().getUniqueId().toString()),
                            Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_ALWAYSON.format("default")));
            Main.playerConfig.config
                    .set(PlayerConfig.SETTING_TRACKING.format(p.getPlayer().getUniqueId().toString()),
                            Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_TRACKING.format("default")));
        }
    }
}
