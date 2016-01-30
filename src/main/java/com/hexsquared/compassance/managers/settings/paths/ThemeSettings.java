package com.hexsquared.compassance.managers.settings.paths;

public class ThemeSettings
{
    public static final String THEME_META_NAME = "themes.%s.meta.name";
    public static final String THEME_META_DESC = "themes.%s.meta.desc";

    public static final String THEME_REPLACER_MAIN_PATTERN_MAP = "themes.%s.patterns.main-pattern-map";

    public static final String THEME_REPLACER_PATTERN_DIRECT_REPLACER = "themes.%s.patterns.direct-pattern.%s"; //id,direction-id

    public static final String THEME_REPLACER_SUBPATTERN_MAP = "themes.%s.patterns.sub-pattern.%s.pattern-map"; //id,sep-id
    public static final String THEME_REPLACER_SUBPATTERN_MAP_REPLACER = "themes.%s.patterns.sub-pattern.%s.%s"; //id,sep-id,replacer-id
}
