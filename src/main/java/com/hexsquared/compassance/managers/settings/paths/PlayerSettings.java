package com.hexsquared.compassance.managers.settings.paths;

import org.bukkit.entity.Player;

import static com.hexsquared.compassance.Compassance.instance;

public class PlayerSettings
{
    public static final String PLAYER_NAME = "players.%s.name";

    public static final String SETTING_SELECTEDTHEME = "players.%s.settings.selectedTheme";
    public static final String SETTING_ENABLE = "players.%s.settings.enableCompass";
    public static final String SETTING_CURSOR = "players.%s.settings.enableCursor";
    public static final String SETTING_ALWAYSON = "players.%s.settings.alwaysOn";
    public static final String SETTING_TRACKING = "players.%s.settings.tracking";

    /**
     * Update the player name in the player config.
     * Create a new profile for the player-uuid if
     * selected theme is not found.
     *
     * @param p Player
     */
    public static void updateProfile(Player p)
    {
        instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.PLAYER_NAME, p.getPlayer().getUniqueId().toString()), p.getName());

        String selectedTheme = instance.configManager.getPlayerSettings().getString(String.format(PlayerSettings.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()));
        if (selectedTheme == null || instance.themeManager.getTheme(selectedTheme) == null)
        {
            instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()), instance.themeManager.getDefaultID());
        }
    }
}
