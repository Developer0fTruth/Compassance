package com.hexsquared.compassance.managers.themes;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.misc.Misc;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

public class ThemeManager {

    private HashMap<String,Theme> themes;

    private String defaultID;

    private boolean defaultHasErrors;

    public ThemeManager()
    {
        defaultID = "default";
    }

    /*
     * Load all themes using the enabled-theme
     * StringList, 'default' is expected to
     * present at all times.
     */
    public void loadThemes()
    {
        themes = new HashMap<>();

        int errors = 0;

        Set<String> allThemes;
        allThemes = Compassance.getConfigManager().getThemeConfig().getConfigurationSection("themes").getKeys(false);

        if (!allThemes.contains(defaultID))
        {
            Misc.logHandle(Level.SEVERE, String.format(
                    "Default theme '%s' is not found. Therefore other themes will not be loaded.", defaultID));
            defaultHasErrors = true;
            return;
        }

        for(String s : allThemes)
        {
            Theme t = new Theme(s);

            if(!t.hasErrors())
            {
                Misc.logHandle(Level.INFO, String.format("Loaded theme %s ID '%s'.", Misc.formatColorString(t.getName()), t.getId()));
                themes.put(s, new Theme(s));
            }
            else
            {
                if(s.equals(defaultID))
                {
                    Misc.logHandle(Level.SEVERE, String.format(
                            "Default theme '%s' is not properly formatted. Therefore other themes will not be loaded.", defaultID));
                    defaultHasErrors = true;
                    return;
                }
                Misc.logHandle(Level.SEVERE, String.format("Theme id '%s' had errors and will not be loaded.", t.getId()));
                errors++;
            }
        }

        Misc.logHandle(Level.INFO, String.format(
                "Successfully loaded %s theme(s) with %s theme-related errors.", themes.size(), errors));
    }

    public void unloadTheme(String s)
    {
        if(themes.containsKey(s))
        {
            themes.remove(s);
        }
    }

    public Theme getTheme(String s)
    {
        if(themes.get(s) == null)
        {
            if(defaultHasErrors)
            {
                Misc.logHandle(Level.SEVERE, String.format(
                        "Attempted to get theme '%s', but default theme '%s' is not properly formatted. " +
                                "Therefore access to other themes will be automatically denied.", s, defaultID));
                return null;
            }
            Misc.logHandle(Level.SEVERE, String.format(
                    "Attempted to get theme '%s', but it is not found.", s));
            return null;
        }
        return themes.get(s);
    }

    public String getDefaultID()
    {
        return defaultID;
    }

    public HashMap<String,Theme> getThemes()
    {
        return themes;
    }
}
