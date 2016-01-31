package com.hexsquared.compassance.managers.themes;

import com.hexsquared.compassance.managers.settings.paths.ThemeSettings;

import java.util.HashMap;

import static com.hexsquared.compassance.Compassance.getConfigManager;

public class Theme {

    private final String id;

    private String meta_name;
    private String meta_desc;

    private String data_main_PatternMap;
    private HashMap<String,String> data_DirectReplacers;

    private HashMap<String,String> data_subPatternMap;
    private HashMap<String,HashMap<String,String>> data_subPatternReplacers;

    private String final_PatternMap;
    private HashMap<String,String> final_DirectReplacers;

    boolean hasError;

    public Theme(String id)
    {
        this.id = id;

        data_DirectReplacers = new HashMap<>();
        data_subPatternMap = new HashMap<>();
        data_subPatternReplacers = new HashMap<>();
        final_DirectReplacers = new HashMap<>();

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
        this.meta_name = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_META_NAME,id));
        this.meta_desc = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_META_DESC,id));
        this.data_main_PatternMap = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_MAIN_PATTERN_MAP,id));

        String[] mainPatternReplacers = data_main_PatternMap.split(";");
        for(String s : mainPatternReplacers)
        {
            if(s.startsWith("<s/"))
            {
                String subPatternMap = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_SUBPATTERN_MAP,id,s));

                String[] subPatternReplacers = subPatternMap.split(";");
                HashMap<String,String> map = new HashMap<>();

                for(String s2 : subPatternReplacers)
                {
                    map.put(s2,getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_SUBPATTERN_MAP_REPLACER,id,s,s2)));
                }

                data_subPatternMap.put(s,subPatternMap);
                data_subPatternReplacers.put(s,map);
            }
            else
            {
                String d_replacer = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_DIRECT_REPLACER,id,s));
                data_DirectReplacers.put(s,d_replacer);
            }
        }

        this.final_PatternMap = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_FINAL_MAIN_PATTERN_MAP,id));
        String[] finalPatternReplacers = final_PatternMap.split(";");

        for(String s : finalPatternReplacers)
        {
            String d_replacer = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_FINAL_DIRECT_REPLACER,id,s));
            final_DirectReplacers.put(s,d_replacer);
        }

        final_DirectReplacers.remove("<str>");
    }

    /*
     * Apply the sub-pattern mapping.
     * Returns full string with everything.
     */
    public String getStringMapFull()
    {
        if(!hasErrors())
        {
            String strMap = data_main_PatternMap;
            for (String s : getData_subPatternMap().keySet()) {
                strMap = strMap.replaceAll(s, getData_subPatternMap().get(s));
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
            String strMap = data_main_PatternMap;

            for (String s : getData_DirectReplacers().keySet())
            {
                strMap = strMap.replaceAll(s, getData_DirectReplacers().get(s));
            }

            for (String s : getData_subPatternMap().keySet()) {
                strMap = strMap.replaceAll(s, getData_subPatternMap().get(s));
            }

            for(String s : getData_DirectReplacers().keySet())
            {
                strMap = strMap.replaceAll(s, getData_DirectReplacers().get(s));
            }

            for(String s : getData_subPatternMap().keySet())
            {
                for(String s2 : getData_subPatternReplacers().get(s).keySet())
                {
                    strMap = strMap.replaceAll(s2, getData_subPatternReplacers().get(s).get(s2));
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
        return this.meta_name;
    }

    public String getId()
    {
        return id;
    }

    public String getDesc()
    {
        return this.meta_desc;
    }

    public String getData_main_PatternMap()
    {
        return this.data_main_PatternMap;
    }

    public HashMap<String, String> getData_DirectReplacers()
    {
        return this.data_DirectReplacers;
    }

    public HashMap<String, String> getData_subPatternMap()
    {
        return data_subPatternMap;
    }

    public HashMap<String,HashMap<String,String>> getData_subPatternReplacers()
    {
        return data_subPatternReplacers;
    }

    public String getFinal_PatternMap()
    {
        return this.final_PatternMap;
    }

    public HashMap<String, String> getFinal_DirectReplacers()
    {
        return this.final_DirectReplacers;
    }

}
