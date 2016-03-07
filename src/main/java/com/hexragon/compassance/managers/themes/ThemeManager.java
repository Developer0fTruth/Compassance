package com.hexragon.compassance.managers.themes;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.files.configs.ThemeConfig;
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
        allThemes.addAll(Main.themeConfig.config.getStringList(ThemeConfig.ENABLED_THEMES.path));

        int i = 0;
        for (String s : allThemes)
        {
            if (i >= 35)
            {
                break;
            }

            Theme t = new Theme(s);
            t.loadData();

            if (t.meta.name == null || t.meta.desc == null)
            {
                if (s.equalsIgnoreCase(defaultID))
                {
                    defaultHasErrors = true;
                    Main.logger.severe("Default theme has errors!");
                    return;
                }
                errors++;
                continue;
            }

            themes.put(s, t);

            i++;
        }
        Main.logger.log(errors >= 1 ? Level.WARNING : Level.INFO, String.format("Successfully loaded %s theme(s) with %s theme-related errors.", themes.size(), errors));
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

    public String getDefaultID()
    {
        return defaultID;
    }

    public LinkedHashMap<String, Theme> getThemes()
    {
        return themes;
    }

    public LinkedHashSet<Theme> themesAccessibleTo(Player p)
    {
        LinkedHashSet<String> idList = new LinkedHashSet<>(getThemes().keySet());
        LinkedHashSet<Theme> themeList = new LinkedHashSet<>(); // If player do not have permission of themes, it is omitted.
        if (Main.mainConfig.config.getBoolean(MainConfig.USE_PERMISSIONS.path))
        {
            for (String id : idList)
            {
                Theme t = getTheme(id);

                if (Utils.permHandle(p, t.meta.permission, true))
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
