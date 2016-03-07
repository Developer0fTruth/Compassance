package com.hexragon.compassance.managers.themes;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.files.configs.ThemeConfig;
import com.hexragon.compassance.managers.compass.CompassGenerator;

import java.util.HashMap;

public class Theme
{
    public final String id;

    public final ThemeMeta meta;
    public final ThemeData data;
    public final ThemeFinal post;
    public final ThemeFunctionals func;

    private final CompassGenerator generator;

    /**
     * Grab all data from themes-configs.configs using designated paths.
     * Associate values with a map so CompassStringGenerator can
     * generated string with styled characters.
     */
    public Theme(String id)
    {
        this.id = id;

        generator = new CompassGenerator(this);

        meta = new ThemeMeta();
        data = new ThemeData();
        post = new ThemeFinal();
        func = new ThemeFunctionals();

        data.replacers = new HashMap<>();
        data.sub.pattern = new HashMap<>();
        data.sub.replacers = new HashMap<>();

        post.replacers = new HashMap<>();
    }

    public void loadData()
    {
        meta.name = Main.themeConfig.config.getString(ThemeConfig.THEME_META_NAME.format(id));
        meta.desc = Main.themeConfig.config.getString(ThemeConfig.THEME_META_DESC.format(id));
        meta.permission = Main.themeConfig.config.getString(ThemeConfig.THEME_META_PERM.format(id));

        try
        {
            meta.patternMap = Main.themeConfig.config.getString(ThemeConfig.THEME_DATA_MAIN_PATTERN_MAP.format(id));

            if (meta.patternMap != null)
            {
                String[] mainPatternReplacers = meta.patternMap.split(";");
                for (String s : mainPatternReplacers)
                {
                    /*
                     & Search in sub-pattern node.
                     */
                    if (s.startsWith("<s/") && s.endsWith(">"))
                    {
                        String subPatternMap = Main.themeConfig.config.getString(ThemeConfig.THEME_DATA_SUBPATTERN_MAP.format(id, s));

                        String[] subPatternReplacers = subPatternMap.split(";");
                        HashMap<String, String> map = new HashMap<>();

                        for (String s2 : subPatternReplacers)
                        {
                            map.put(s2, Main.themeConfig.config.getString(ThemeConfig.THEME_DATA_SUBPATTERN_MAP_REPLACER.format(id, s, s2)));
                        }

                        data.sub.pattern.put(s, subPatternMap);
                        data.sub.replacers.put(s, map);
                    }

                    /*
                     * Search in direct node.
                     */
                    else if (s.startsWith("<") && s.endsWith(">"))
                    {
                        String d_replacer = Main.themeConfig.config.getString(ThemeConfig.THEME_DATA_DIRECT_REPLACER.format(id, s));
                        data.replacers.put(s, d_replacer);
                    }
                }
            }
            else
            {
                return;
            }

        /*
         * Search in final node.
         */
            post.pattern = Main.themeConfig.config.getString(ThemeConfig.THEME_FINAL_MAIN_PATTERN_MAP.format(id));
            String[] finalPatternReplacers = post.pattern.split(";");

            for (String s : finalPatternReplacers)
            {
                if (s.startsWith("<") && s.endsWith(">"))
                {
                    String d_replacer = Main.themeConfig.config.getString(ThemeConfig.THEME_FINAL_DIRECT_REPLACER.format(id, s));
                    post.replacers.put(s, d_replacer);
                }
            }

            func.cursor = Main.themeConfig.config.getString(ThemeConfig.THEME_DATA_FUNC_CURSOR.format(id));
            func.target = Main.themeConfig.config.getString(ThemeConfig.THEME_DATA_FUNC_TARGET.format(id));
        }
        catch (Exception ignored)
        {
        }
    }

    /**
     * Apply the sub-pattern mapping.
     * Returns full string with everything.
     */
    public String getStringMapFull()
    {
        String strMap = meta.patternMap;
        for (String s : data.sub.pattern.keySet())
        {
            strMap = strMap.replaceAll(s, data.sub.pattern.get(s));
        }
        return strMap;
    }

    /**
     * @return Every replacing node of the theme.
     */
    public String[] getStringMapArray()
    {
        return getStringMapFull().split(";");
    }

    public CompassGenerator getGenerator()
    {
        return generator;
    }

    public class ThemeMeta
    {
        public String name;
        public String desc;
        public String permission;
        public String patternMap;
    }

    public class ThemeData
    {
        public final ThemeSubData sub = new ThemeSubData();

        public HashMap<String, String> replacers;

        public class ThemeSubData
        {
            public HashMap<String, String> pattern;
            public HashMap<String, HashMap<String, String>> replacers;
        }
    }

    public class ThemeFinal
    {
        public String pattern;
        public HashMap<String, String> replacers;
    }

    public class ThemeFunctionals
    {
        public String cursor;
        public String target;
    }

}
