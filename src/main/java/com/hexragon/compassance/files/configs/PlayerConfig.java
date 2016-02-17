package com.hexragon.compassance.files.configs;

import com.hexragon.compassance.Compassance;
import org.bukkit.entity.Player;

public class PlayerConfig
{
    public static final String PLAYER_NAME = "players.%s.name";

    public static final String SETTING_SELECTEDTHEME = "players.%s.settings.selectedTheme";
    public static final String SETTING_ENABLE = "players.%s.settings.enableCompass";
    public static final String SETTING_CURSOR = "players.%s.settings.enableCursor";
    public static final String SETTING_ALWAYSON = "players.%s.settings.alwaysOn";
    public static final String SETTING_TRACKING = "players.%s.settings.tracking";

    public static void updateProfile(Player p)
    {
        Compassance.playerConfig.config.set(String.format(PlayerConfig.PLAYER_NAME, p.getPlayer().getUniqueId().toString()), p.getName());

        String selectedTheme = Compassance.playerConfig.config.getString(String.format(PlayerConfig.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()));
        if (selectedTheme == null || Compassance.themeManager.getTheme(selectedTheme) == null)
        {
            Compassance.playerConfig.config
                    .set(String.format(PlayerConfig.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()), Compassance.themeManager.getDefaultID());

            Compassance.playerConfig.config
                    .set(String.format(PlayerConfig.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()),
                            Compassance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_ENABLE, "default")));
            Compassance.playerConfig.config
                    .set(String.format(PlayerConfig.SETTING_CURSOR, p.getPlayer().getUniqueId().toString()),
                            Compassance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_CURSOR, "default")));
            Compassance.playerConfig.config
                    .set(String.format(PlayerConfig.SETTING_ALWAYSON, p.getPlayer().getUniqueId().toString()),
                            Compassance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_ALWAYSON, "default")));
            Compassance.playerConfig.config
                    .set(String.format(PlayerConfig.SETTING_TRACKING, p.getPlayer().getUniqueId().toString()),
                            Compassance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_TRACKING, "default")));
        }
    }
}
