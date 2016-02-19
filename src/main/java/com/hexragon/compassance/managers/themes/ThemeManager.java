package com.hexragon.compassance.managers.themes;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.files.configs.ThemeConfig;
import com.hexragon.compassance.misc.Utils;

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

    /**
     * Load all themes using the enabled-theme
     * StringList, 'default' is expected to
     * present at all times.
     */
    public void loadThemes()
    {
        themes = new LinkedHashMap<>();

        int errors = 0;

        LinkedHashSet<String> allThemes = new LinkedHashSet<>();
        allThemes.add("default");
        allThemes.addAll(Compassance.themeConfig.config.getStringList(ThemeConfig.ENABLED_THEMES.path));

        int i = 0;
        for (String s : allThemes)
        {
            if (i >= 35)
            {
                break;
            }

            Theme t = new Theme(s);

            if (t.meta.name == null || t.meta.desc == null)
            {
                if (s.equalsIgnoreCase(defaultID))
                {
                    defaultHasErrors = true;
                    Utils.logHandle(Level.SEVERE, "Default theme has errors!");
                    return;
                }
                errors++;
                continue;
            }

            themes.put(s, t);

            i++;
        }
        Utils.logHandle(errors >= 1 ? Level.WARNING : Level.INFO, String.format("Successfully loaded %s theme(s) with %s theme-related errors.", themes.size(), errors));
    }

    /**
     * Grabs the theme instance.
     *
     * @param s Theme id
     *
     * @return Theme instance
     */
    public Theme getTheme(String s)
    {
        if (themes.get(s) == null)
        {
            if (defaultHasErrors)
            {
                Utils.logHandle(Level.SEVERE, String.format("Attempted to get theme '%s', but default theme '%s' is not properly formatted. " + "Therefore access to other themes will be automatically denied.", s, defaultID));
                return null;
            }
            Utils.logHandle(Level.SEVERE, String.format("Attempted to get theme '%s', but it is not found.", s));
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
}
