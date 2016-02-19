package com.hexragon.compassance.misc;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.files.configs.PlayerConfig;
import com.hexragon.compassance.managers.compass.generator.GeneratorInfo;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class Utils
{

    public static String fmtClr(String s)
    {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String fmtRefs(GeneratorInfo gi, String s)
    {
        Date d = new Date();

        s = s.replaceAll("%theme-id%", gi.theme.id)
                .replaceAll("%theme%", gi.theme.meta.name)
        ;

        // TIME
        //Day of Week ex. Tues, Monday
        s = s.replaceAll("%dow%", new SimpleDateFormat("EEEE").format(d))
                .replaceAll("%a/dow%", new SimpleDateFormat("E").format(d))

                //Date
                .replaceAll("%month%", new SimpleDateFormat("MMMM").format(d))
                .replaceAll("%a/month%", new SimpleDateFormat("MMM").format(d))
                .replaceAll("%n/month%", new SimpleDateFormat("M").format(d))
                .replaceAll("%day%", new SimpleDateFormat("d").format(d))

                //Time
                .replaceAll("%time-zone%", new SimpleDateFormat("z").format(d))
                .replaceAll("%marker%", new SimpleDateFormat("a").format(d))
                .replaceAll("%second%", new SimpleDateFormat("ss").format(d))
                .replaceAll("%minute%", new SimpleDateFormat("mm").format(d))
                .replaceAll("%hour%", new SimpleDateFormat("h").format(d))
        ;

        // LOCATION
        Location loc = gi.p.getLocation();
        s = s.replaceAll("%x%", String.valueOf(loc.getBlockX()))
                .replaceAll("%y%", String.valueOf(loc.getBlockY()))
                .replaceAll("%z%", String.valueOf(loc.getBlockZ()))
        ;

        // YAW
        double yaw = 360 + gi.yaw;
        if (yaw > 360) yaw -= 360;
        s = s.replaceAll("%yaw%", String.format("%.2f", yaw));

        if (Compassance.placeholderAPI) s = PlaceholderAPI.setPlaceholders(gi.p, s);

        return s;
    }

    public static void logHandle(Level l, String msg)
    {
        String note = "|+| ";
        if (l == Level.SEVERE || l == Level.WARNING)
        {
            note = "!#! ";
        }
        Compassance.instance.getLogger().log(l, note + msg);
    }

    public static boolean permHandle(Player p, String perm, boolean ifPermIsNull)
    {
        return perm != null && p.hasPermission(perm) || perm == null && ifPermIsNull;
    }

    public static void updateProfile(Player p)
    {
        Compassance.playerConfig.config.set(PlayerConfig.PLAYER_NAME.format(p.getPlayer().getUniqueId().toString()), p.getName());

        String selectedTheme = Compassance.playerConfig.config.getString(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString()));
        if (selectedTheme == null || Compassance.themeManager.getTheme(selectedTheme) == null)
        {
            Compassance.playerConfig.config
                    .set(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString()), Compassance.themeManager.getDefaultID());

            Compassance.playerConfig.config
                    .set(PlayerConfig.SETTING_ENABLE.format(p.getPlayer().getUniqueId().toString()),
                            Compassance.playerConfig.config.getBoolean(PlayerConfig.SETTING_ENABLE.format("default")));
            Compassance.playerConfig.config
                    .set(PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()),
                            Compassance.playerConfig.config.getBoolean(PlayerConfig.SETTING_CURSOR.format("default")));
            Compassance.playerConfig.config
                    .set(PlayerConfig.SETTING_ALWAYSON.format(p.getPlayer().getUniqueId().toString()),
                            Compassance.playerConfig.config.getBoolean(PlayerConfig.SETTING_ALWAYSON.format("default")));
            Compassance.playerConfig.config
                    .set(PlayerConfig.SETTING_TRACKING.format(p.getPlayer().getUniqueId().toString()),
                            Compassance.playerConfig.config.getBoolean(PlayerConfig.SETTING_TRACKING.format("default")));
        }
    }
}
