package com.hexragon.compassance.configs;

public class ConfigurationPaths
{
    public enum MainConfig
    {
        USE_PERMISSIONS("use-permissions"),
        USE_TRACKING("use-tracking"),
        METRICS("metrics"),
        DEBUG_MODE("bleeding-edge"),
        UPDATE_CHECK("update-checker"),
        CHECKER_INTERVAL("checker-interval");

        public final String path;

        MainConfig(String s)
        {
            path = s;
        }

        public String format(Object... obj)
        {
            return String.format(path, obj);
        }
    }

    public enum PlayerConfig
    {
        PLAYER_NAME("players.%s.name"),

        SETTING_SELECTEDTHEME("players.%s.settings.selectedTheme"),
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

    public enum ThemeConfig
    {
        ENABLED_THEMES("enabled-themes"),

        THEME_META_NAME("themes.%s.meta.name"),
        THEME_META_DESC("themes.%s.meta.desc"),
        THEME_META_PERM("themes.%s.meta.permission"),

        THEME_DATA_MAIN_PATTERN_MAP("themes.%s.data.main-pattern-map"),

        THEME_DATA_FUNC_CURSOR("themes.%s.data.function.cursor"),
        THEME_DATA_FUNC_TARGET("themes.%s.data.function.target"),

        THEME_DATA_DIRECT_REPLACER("themes.%s.data.direct.%s"), //id,replacer,id

        THEME_DATA_SUBPATTERN_MAP("themes.%s.data.sub-pattern.%s.pattern-map"), //id,sep-id
        THEME_DATA_SUBPATTERN_MAP_REPLACER("themes.%s.data.sub-pattern.%s.%s"), //id,sep-id,replacer-id

        THEME_FINAL_MAIN_PATTERN_MAP("themes.%s.final.pattern-map"),
        THEME_FINAL_DIRECT_REPLACER("themes.%s.final.%s");

        public final String path;

        ThemeConfig(String s)
        {
            path = s;
        }

        public String format(Object... obj)
        {
            return String.format(path, obj);
        }
    }
}
