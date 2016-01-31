package com.hexsquared.compassance.managers.settings.paths;

import org.bukkit.entity.Player;

import static com.hexsquared.compassance.Compassance.getConfigManager;
import static com.hexsquared.compassance.Compassance.getThemeManager;

public class PlayerSettings
{
    public static final String PLAYER_NAME = "players.%s.name";

    public static final String THEME_SELECTED = "players.%s.settings.selectedTheme";
    public static final String COMPASS_ENABLE = "players.%s.settings.enableCompass";
    public static final String COMPASS_CUSROR = "players.%s.settings.enableCursor";

    public static void updateProfile(Player p)
    {
        getConfigManager().getPlayerSettings().set(String.format(PlayerSettings.PLAYER_NAME, p.getPlayer().getUniqueId().toString()), p.getName());

        String selectedTheme = getConfigManager().getPlayerSettings().getString(String.format(PlayerSettings.THEME_SELECTED, p.getPlayer().getUniqueId().toString()));
        if (selectedTheme == null || getThemeManager().getTheme(selectedTheme) == null)
        {
            getConfigManager().getPlayerSettings().set(String.format(PlayerSettings.THEME_SELECTED, p.getPlayer().getUniqueId().toString()), getThemeManager().getDefaultID());
        }
    }
}
