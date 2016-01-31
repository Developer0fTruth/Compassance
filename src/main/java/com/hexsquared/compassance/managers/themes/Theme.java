package com.hexsquared.compassance.managers.themes;

import com.hexsquared.compassance.managers.settings.paths.ThemeSettings;

import java.util.HashMap;

import static com.hexsquared.compassance.Compassance.getConfigManager;

public class Theme {

    private final String id;

    private String name;
    private String desc;

    private String main_PatternMap;
    private HashMap<String,String> pattern_subPatternMap;

    private HashMap<String,String> pattern_DirectReplacers;
    private HashMap<String,HashMap<String,String>> pattern_subPatternReplacers;

    boolean hasError;

    public Theme(String id)
    {
        this.id = id;

        pattern_DirectReplacers = new HashMap<>();
        pattern_subPatternMap = new HashMap<>();
        pattern_subPatternReplacers = new HashMap<>();

        loadData();
        hasError = testForErrors();
    }

    /*
     * Grab all data from themes-config.yml using designated paths.
     * Associate values with a map so CompassStringGenerator can
     * -generated string with styled characters.
     */
    public void loadData()
    {
        this.name = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_META_NAME,id));
        this.desc = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_META_DESC,id));
        this.main_PatternMap = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_MAIN_PATTERN_MAP,id));

        String[] mainPatternReplacers = main_PatternMap.split(";");
        for(String s : mainPatternReplacers)
        {
            if(s.startsWith("<d/"))
            {
                String d_replacer = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_DIRECT_REPLACER,id,s));
                pattern_DirectReplacers.put(s,d_replacer);
            }
            if(s.startsWith("<s/"))
            {
                String subPatternMap = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_SUBPATTERN_MAP,id,s));

                String[] subPatternReplacers = subPatternMap.split(";");
                HashMap<String,String> map = new HashMap<>();

                for(String s2 : subPatternReplacers)
                {
                    map.put(s2,getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_SUBPATTERN_MAP_REPLACER,id,s,s2)));
                }

                pattern_subPatternMap.put(s,subPatternMap);
                pattern_subPatternReplacers.put(s,map);
            }
        }
    }

    /*
     * Apply the sub-pattern mapping.
     * Returns full string with everything.
     */
    public String getStringMapFull()
    {
        if(!hasErrors())
        {
            String strMap = main_PatternMap;
            for (String s : getPattern_subPatternMap().keySet()) {
                strMap = strMap.replaceAll(s, getPattern_subPatternMap().get(s));
            }
            return strMap;
        }
        return null;
    }

    public String[] getStringMapArray()
    {
        if(!hasErrors())
        {
            return getStringMapFull().split(";");
        }
        return null;
    }

    /*
     * Attempts to parse the whole string.
     * If a NullPointerException is thrown, it will
     * return a true, meaning there IS errors.
     */
    public boolean hasErrors()
    {
        return hasError;
    }

    private boolean testForErrors()
    {
        try
        {
            String strMap = main_PatternMap;

            for (String s : getPattern_DirectReplacers().keySet())
            {
                strMap = strMap.replaceAll(s, getPattern_DirectReplacers().get(s));
            }

            for (String s : getPattern_subPatternMap().keySet()) {
                strMap = strMap.replaceAll(s, getPattern_subPatternMap().get(s));
            }

            for(String s : getPattern_DirectReplacers().keySet())
            {
                strMap = strMap.replaceAll(s,getPattern_DirectReplacers().get(s));
            }

            for(String s : getPattern_subPatternMap().keySet())
            {
                for(String s2 : getPattern_subPatternReplacers().get(s).keySet())
                {
                    strMap = strMap.replaceAll(s2,getPattern_subPatternReplacers().get(s).get(s2));
                }
            }
        }
        catch(NullPointerException e)
        {
            return true;
        }
        return false;
    }

    public String getName()
    {
        return this.name;
    }

    public String getId()
    {
        return id;
    }

    public String getDesc()
    {
        return this.desc;
    }

    public String getMain_PatternMap()
    {
        return this.main_PatternMap;
    }

    public HashMap<String, String> getPattern_DirectReplacers()
    {
        return this.pattern_DirectReplacers;
    }

    public HashMap<String, String> getPattern_subPatternMap()
    {
        return pattern_subPatternMap;
    }

    public HashMap<String,HashMap<String,String>> getPattern_subPatternReplacers()
    {
        return pattern_subPatternReplacers;
    }

}
