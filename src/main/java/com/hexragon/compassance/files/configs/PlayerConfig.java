package com.hexragon.compassance.files.configs;

public enum PlayerConfig
{
    PLAYER_NAME("players.%s.name"),

    SETTING_SELECTEDTHEME("players.%s.settings.selectedTheme"),
    SETTING_POSITION("players.%s.settings.position"),
    SETTING_ENABLE("players.%s.settings.enableCompass"),
    SETTING_CURSOR("players.%s.settings.enableCursor"),
    SETTING_ALWAYSON("players.%s.settings.alwaysOn"),
    SETTING_TRACKING("players.%s.settings.tracking");

    public final String path;

    PlayerConfig(String s)
    {
        path = s;
    }

    public String format(Object... obj)
    {
        return String.format(path, obj);
    }
}
