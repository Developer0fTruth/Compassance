package com.hexragon.compassance.utils;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.files.Gearbox;
import com.hexragon.compassance.files.configs.PlayerConfig;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

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

    public static void gbVersionCheck(Gearbox gb)
    {
        FileConfiguration config = gb.config;

        if (!config.getDefaults().getString("version").equals(config.getString("version")) || config.getString("version") == null)
        {
            Main.logger.warning(String.format("%s is outdated, compatibility problems may occur.", gb.fileName));
        }
        else
        {
            Main.logger.info(String.format("%s config is up to date.", gb.fileName));
        }
    }


    public static String onlineUpdateCheck()
    {
        String urlStr = "https://raw.githubusercontent.com/Hexragon/Compassance/master/latest-version";
        String text = null;
        try
        {
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(urlStr).openStream(), Charset.defaultCharset()));
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null)
                {
                    stringBuilder.append(str);
                }
                text = stringBuilder.toString();
                reader.close();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            if (text != null)
            {
                return text;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "error";
        }
        return "null";
    }
}
