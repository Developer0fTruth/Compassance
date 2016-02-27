package com.hexragon.compassance;

import com.hexragon.compassance.files.configs.PlayerConfig;
import com.hexragon.compassance.managers.compass.CompassGenerator;
import com.hexragon.compassance.managers.tasks.tracking.TrackedTarget;
import com.hexragon.compassance.managers.themes.Theme;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class Placeholders extends PlaceholderHook
{
    public Placeholders(Main instance)
    {
        if (PlaceholderAPI.registerPlaceholderHook(instance, this))
        {
            instance.getLogger().info("Registered placeholders to PlaceholderAPI.");
        }
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier)
    {
        if (p == null)
        {
            return null;
        }

        if (identifier.equals("p_selectedtheme"))
        {
            return Main.playerConfig.config.getString(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getUniqueId()));
        }

        if (identifier.equals("p_target"))
        {
            TrackedTarget t = Main.trackingManager.getTargetOf(p);

            if (t == null || t.getLocation() == null)
            {
                return "None";
            }

            if (t.getType() == TrackedTarget.TrackType.DYNAMIC)
            {
                return t.getTarget().getName();
            }
            else if (t.getType() == TrackedTarget.TrackType.STATIC)
            {
                return "Location";
            }
        }

        if (identifier.equals("p_target_location"))
        {
            TrackedTarget t = Main.trackingManager.getTargetOf(p);

            if (t == null || t.getLocation() == null)
            {
                return "None";
            }

            Location l = t.getLocation();
            return String.format("%d %d %d", l.getBlockX(), l.getBlockY(), l.getBlockZ());
        }

        if (identifier.equals("p_target_distance"))
        {
            TrackedTarget t = Main.trackingManager.getTargetOf(p);

            if (t == null || t.getLocation() == null)
            {
                return "N/A";
            }

            if (p.getLocation().getWorld() != t.getLocation().getWorld())
            {
                return "Otherworldly";
            }

            long distance = Math.round(p.getLocation().distance(t.getLocation()));

            return String.valueOf(distance) + "m";
        }


        if (identifier.equals("p_string"))
        {
            String id = Main.playerConfig.config.getString(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()));
            boolean cursor = Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()));
            Theme theme = Main.themeManager.getTheme(id);

            if (theme == null)
            {
                theme = Main.themeManager.getTheme(Main.themeManager.defaultID);
            }

            CompassGenerator.GeneratorInfo gi;
            TrackedTarget target = Main.trackingManager.getTargetOf(p);
            if (target != null && target.getLocation() != null)
            {
                gi = new CompassGenerator.GeneratorInfo(p, p.getLocation(), target.getLocation(), p.getLocation().getYaw(), cursor);
            }
            else
            {
                gi = new CompassGenerator.GeneratorInfo(p, null, null, p.getLocation().getYaw(), cursor);
            }
            if (theme.getGenerator().getString(gi) != null) return theme.getGenerator().getString(gi);
        }

        if (identifier.startsWith("p_string_theme_"))
        {
            String id = identifier.replace("p_string_theme_", "");
            boolean cursor = Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()));
            Theme theme = Main.themeManager.getTheme(id);

            if (theme == null)
            {
                return "Theme doesn't exist!";
            }

            CompassGenerator.GeneratorInfo gi;
            TrackedTarget target = Main.trackingManager.getTargetOf(p);
            if (target != null && target.getLocation() != null)
            {
                gi = new CompassGenerator.GeneratorInfo(p, p.getLocation(), target.getLocation(), p.getLocation().getYaw(), cursor);
            }
            else
            {
                gi = new CompassGenerator.GeneratorInfo(p, null, null, p.getLocation().getYaw(), cursor);
            }
            if (theme.getGenerator().getString(gi) != null) return theme.getGenerator().getString(gi);
        }

        return null;
    }
}