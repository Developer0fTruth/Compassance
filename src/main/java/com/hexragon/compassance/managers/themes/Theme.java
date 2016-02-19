package com.hexragon.compassance.managers.themes;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.files.configs.ThemeConfig;

import java.util.HashMap;

public class Theme
{
    public final String id;

    public ThemeMeta meta;
    public ThemeData data;
    public ThemeFinal post;
    public ThemeFunctionals func;

    /**
     * Grab all data from themes-configs.configs using designated paths.
     * Associate values with a map so CompassStringGenerator can
     * generated string with styled characters.
     */
    public Theme(String id)
    {
        this.id = id;

        meta = new ThemeMeta();
        data = new ThemeData();
        post = new ThemeFinal();
        func = new ThemeFunctionals();

        data.replacers = new HashMap<>();
        data.sub.pattern = new HashMap<>();
        data.sub.replacers = new HashMap<>();

        post.replacers = new HashMap<>();

        loadData();
    }

    public void loadData()
    {
        meta.name = Compassance.themeConfig.config.getString(ThemeConfig.THEME_META_NAME.format(id));
        meta.desc = Compassance.themeConfig.config.getString(ThemeConfig.THEME_META_DESC.format(id));
        meta.permission = Compassance.themeConfig.config.getString(ThemeConfig.THEME_META_PERM.format(id));

        try
        {
            meta.patternMap = Compassance.themeConfig.config.getString(ThemeConfig.THEME_DATA_MAIN_PATTERN_MAP.format(id));

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
                        String subPatternMap = Compassance.themeConfig.config.getString(ThemeConfig.THEME_DATA_SUBPATTERN_MAP.format(id, s));

                        String[] subPatternReplacers = subPatternMap.split(";");
                        HashMap<String, String> map = new HashMap<>();

                        for (String s2 : subPatternReplacers)
                        {
                            map.put(s2, Compassance.themeConfig.config.getString(ThemeConfig.THEME_DATA_SUBPATTERN_MAP_REPLACER.format(id, s, s2)));
                        }

                        data.sub.pattern.put(s, subPatternMap);
                        data.sub.replacers.put(s, map);
                    }

                    /*
                     * Search in direct node.
                     */
                    else if (s.startsWith("<") && s.endsWith(">"))
                    {
                        String d_replacer = Compassance.themeConfig.config.getString(ThemeConfig.THEME_DATA_DIRECT_REPLACER.format(id, s));
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
            post.pattern = Compassance.themeConfig.config.getString(ThemeConfig.THEME_FINAL_MAIN_PATTERN_MAP.format(id));
            String[] finalPatternReplacers = post.pattern.split(";");

            for (String s : finalPatternReplacers)
            {
                if (s.startsWith("<") && s.endsWith(">"))
                {
                    String d_replacer = Compassance.themeConfig.config.getString(ThemeConfig.THEME_FINAL_DIRECT_REPLACER.format(id,s));
                    post.replacers.put(s, d_replacer);
                }
            }

            func.cursor = Compassance.themeConfig.config.getString(ThemeConfig.THEME_DATA_FUNC_CURSOR.format(id));
            func.target = Compassance.themeConfig.config.getString(ThemeConfig.THEME_DATA_FUNC_TARGET.format(id));
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

    public class ThemeMeta
    {
        public String name;
        public String desc;
        public String permission;
        public String patternMap;
    }

    public class ThemeData
    {
        public ThemeSubData sub = new ThemeSubData();

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
