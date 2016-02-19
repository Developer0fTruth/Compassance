package com.hexragon.compassance;

import com.hexragon.compassance.files.configs.PlayerConfig;
import com.hexragon.compassance.managers.compass.generator.CompassStringGenerator;
import com.hexragon.compassance.managers.compass.generator.GeneratorInfo;
import com.hexragon.compassance.managers.compass.tasks.tracking.TrackedTarget;
import com.hexragon.compassance.managers.compass.tasks.tracking.TrackingType;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.misc.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CompassancePlaceholderHook extends me.clip.placeholderapi.PlaceholderHook
{
    public CompassancePlaceholderHook(Compassance instance)
    {
        if (PlaceholderAPI.registerPlaceholderHook(instance, this))
        {
            Utils.logHandle(Level.INFO, "Registered placeholders to PlaceholderAPI.");
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

            return Compassance.playerConfig.config.getString(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()));
        }

        if (identifier.equals("p_target"))
        {
            TrackedTarget t = Compassance.trackingManager.getTargetOf(p);

            if (t == null || t.getLocation() == null)
            {
                return "None";
            }

            if (t.getType() == TrackingType.DYNAMIC)
            {
                return t.getTarget().getName();
            }
            else if (t.getType() == TrackingType.STATIC)
            {
                return "Location";
            }
        }

        if (identifier.equals("p_target_location"))
        {
            TrackedTarget t = Compassance.trackingManager.getTargetOf(p);

            if (t == null || t.getLocation() == null)
            {
                return "None";
            }

            Location l = t.getLocation();
            return String.format("%.2f %.2f %.2f", l.getX(), l.getY(), l.getZ());
        }

        if (identifier.equals("p_string"))
        {
            String theme = Compassance.playerConfig.config.getString(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()));
            boolean cursor = Compassance.playerConfig.config.getBoolean(PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()));
            Theme th = Compassance.themeManager.getTheme(theme);

            if (th == null)
            {
                th = Compassance.themeManager.getTheme(Compassance.themeManager.defaultID);
            }

            CompassStringGenerator gen;
            TrackedTarget target = Compassance.trackingManager.getTargetOf(p);
            if (target != null && target.getLocation() != null)
            {
                GeneratorInfo gi = new GeneratorInfo(p, p.getLocation(), target.getLocation(), th, p.getLocation().getYaw(), cursor);
                gen = new CompassStringGenerator(gi);
            }
            else
            {
                GeneratorInfo gi = new GeneratorInfo(p, null, null, th, p.getLocation().getYaw(), cursor);
                gen = new CompassStringGenerator(gi);
            }
            return gen.getString();
        }

        if (identifier.startsWith("p_string_theme_"))
        {
            String theme = identifier.replace("p_string_theme_", "");
            boolean cursor = Compassance.playerConfig.config.getBoolean(PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()));
            Theme th = Compassance.themeManager.getTheme(theme);

            if (th == null)
            {
                return "Theme doesn't exist!";
            }

            CompassStringGenerator gen;
            TrackedTarget target = Compassance.trackingManager.getTargetOf(p);
            if (target != null && target.getLocation() != null)
            {
                GeneratorInfo gi = new GeneratorInfo(p, p.getLocation(), target.getLocation(), th, p.getLocation().getYaw(), cursor);
                gen = new CompassStringGenerator(gi);
            }
            else
            {
                GeneratorInfo gi = new GeneratorInfo(p, null, null, th, p.getLocation().getYaw(), cursor);
                gen = new CompassStringGenerator(gi);
            }
            return gen.getString();
        }

        return null;
    }
}