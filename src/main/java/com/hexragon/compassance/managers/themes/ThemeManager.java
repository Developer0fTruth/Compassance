package com.hexragon.compassance.managers.themes;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.configs.ConfigurationPaths;
import com.hexragon.compassance.managers.themes.replacers.ReplacersTheme;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.logging.Level;

public class ThemeManager
{
    public final String defaultID;
    private LinkedHashMap<String, Theme> themes;
    private boolean defaultHasErrors;

    public ThemeManager()
    {
        defaultID = "default";
    }

    public void loadThemes()
    {
        themes = new LinkedHashMap<>();

        int errors = 0;

        LinkedHashSet<String> allThemes = new LinkedHashSet<>();
        allThemes.add("default");
        allThemes.addAll(Main.themeConfig.config.getStringList(ConfigurationPaths.ThemeConfig.ENABLED_THEMES.path));

        int i = 0;
        for (String s : allThemes)
        {
            if (i >= 35)
            {
                break;
            }

            Theme t = new ReplacersTheme(s);
            t.loadData();

            if (t.main.name == null || t.main.desc == null)
            {
                errors++;
                continue;
            }

            themes.put(s, t);

            i++;
        }
        Main.logger.log(errors >= 1 ? Level.WARNING : Level.INFO, String.format("Successfully loaded %s theme(s) with %s theme-related errors.", themes.size(), errors));
    }

    public boolean addTheme(Theme t)
    {
        try
        {
            themes.put(t.id, t);
            t.loadData();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Theme getTheme(String s)
    {
        if (themes.get(s) == null)
        {
            if (defaultHasErrors)
            {
                Main.logger.severe(String.format("Attempted to get theme '%s', but default theme '%s' is not properly formatted. " + "Therefore access to other themes will be automatically denied.", s, defaultID));
                return null;
            }
            Main.logger.severe(String.format("Attempted to get theme '%s', but it is not found.", s));
            return null;
        }
        return themes.get(s);
    }

    public LinkedHashMap<String, Theme> getThemes()
    {
        return themes;
    }

    public LinkedHashSet<Theme> themesAccessibleTo(Player p)
    {
        LinkedHashSet<String> idList = new LinkedHashSet<>(getThemes().keySet());
        LinkedHashSet<Theme> themeList = new LinkedHashSet<>(); // If player do not have permission of themes, it is omitted.
        if (Main.mainConfig.config.getBoolean(ConfigurationPaths.MainConfig.USE_PERMISSIONS.path))
        {
            for (String id : idList)
            {
                Theme t = getTheme(id);

                if (Utils.permHandle(p, t.main.permission, true) || Utils.permHandle(p, "compassance.theme.*", false))
                {
                    themeList.add(t);
                }
            }
        }
        else
        {
            for (String id : idList)
            {
                Theme t = getTheme(id);
                themeList.add(t);
            }
        }
        return themeList;
    }
}
