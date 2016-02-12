package com.hexragon.compassance.managers.settings.paths;

import com.hexragon.compassance.Compassance;
import org.bukkit.entity.Player;

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
        Compassance.instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.PLAYER_NAME, p.getPlayer().getUniqueId().toString()), p.getName());

        String selectedTheme = Compassance.instance.configManager.getPlayerSettings().getString(String.format(PlayerSettings.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()));
        if (selectedTheme == null || Compassance.instance.themeManager.getTheme(selectedTheme) == null)
        {
            Compassance.instance.configManager.getPlayerSettings()
                    .set(String.format(PlayerSettings.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()), Compassance.instance.themeManager.getDefaultID());

            Compassance.instance.configManager.getPlayerSettings()
                    .set(String.format(PlayerSettings.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()),
                            Compassance.instance.configManager.getPlayerSettings().getBoolean(String.format(PlayerSettings.SETTING_ENABLE, "default")));
            Compassance.instance.configManager.getPlayerSettings()
                    .set(String.format(PlayerSettings.SETTING_CURSOR, p.getPlayer().getUniqueId().toString()),
                            Compassance.instance.configManager.getPlayerSettings().getBoolean(String.format(PlayerSettings.SETTING_CURSOR, "default")));
            Compassance.instance.configManager.getPlayerSettings()
                    .set(String.format(PlayerSettings.SETTING_ALWAYSON, p.getPlayer().getUniqueId().toString()),
                            Compassance.instance.configManager.getPlayerSettings().getBoolean(String.format(PlayerSettings.SETTING_ALWAYSON, "default")));
            Compassance.instance.configManager.getPlayerSettings()
                    .set(String.format(PlayerSettings.SETTING_TRACKING, p.getPlayer().getUniqueId().toString()),
                            Compassance.instance.configManager.getPlayerSettings().getBoolean(String.format(PlayerSettings.SETTING_TRACKING, "default")));
        }
    }
}
