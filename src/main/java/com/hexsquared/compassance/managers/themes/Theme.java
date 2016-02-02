package com.hexsquared.compassance.managers.themes;

import com.hexsquared.compassance.managers.settings.paths.ThemeSettings;

import java.util.HashMap;

import static com.hexsquared.compassance.Compassance.getConfigManager;

public class Theme {

    private final String id;

    private String meta_name;
    private String meta_desc;
    private String meta_perm;

    private String func_cursor;
    private String func_target;

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

    /**
     * Grab all data from themes-config.yml using designated paths.
     * Associate values with a map so CompassStringGenerator can
     * generated string with styled characters.
     */
    public void loadData()
    {
        try {
            this.meta_name = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_META_NAME, id));
            this.meta_desc = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_META_DESC, id));
            this.meta_perm = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_META_PERM, id));

            this.data_main_PatternMap = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_MAIN_PATTERN_MAP, id));
            if (data_main_PatternMap != null)
            {
                String[] mainPatternReplacers = data_main_PatternMap.split(";");
                for (String s : mainPatternReplacers)
                {
                    /*
                     & Search in sub-pattern node.
                     */
                    if (s.startsWith("<s/"))
                    {
                        String subPatternMap = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_SUBPATTERN_MAP, id, s));

                        String[] subPatternReplacers = subPatternMap.split(";");
                        HashMap<String, String> map = new HashMap<>();

                        for (String s2 : subPatternReplacers)
                        {
                            map.put(s2, getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_SUBPATTERN_MAP_REPLACER, id, s, s2)));
                        }

                        data_subPatternMap.put(s, subPatternMap);
                        data_subPatternReplacers.put(s, map);
                    }

                    /*
                     * Search in direct node.
                     */
                    else
                    {
                        String d_replacer = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_DIRECT_REPLACER, id, s));
                        data_DirectReplacers.put(s, d_replacer);
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
            final_PatternMap = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_FINAL_MAIN_PATTERN_MAP, id));
            String[] finalPatternReplacers = final_PatternMap.split(";");

            for (String s : finalPatternReplacers) {
                String d_replacer = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_FINAL_DIRECT_REPLACER, id, s));
                final_DirectReplacers.put(s, d_replacer);
            }

            final_DirectReplacers.remove("<str>");

            func_cursor = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_FUNC_CURSOR, id));
            func_target = getConfigManager().getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_FUNC_TARGET, id));
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
        if(!haveErrors())
        {
            String strMap = data_main_PatternMap;
            for (String s : getData_subPatternMap().keySet()) {
                strMap = strMap.replaceAll(s, getData_subPatternMap().get(s));
            }
            return strMap;
        }
        return null;
    }


    /**
     * @return Every replacing node of the theme.
     */
    public String[] getStringMapArray()
    {
        if(!haveErrors())
        {
            return getStringMapFull().split(";");
        }
        return null;
    }

    /**
     * Attempts to parse the whole string.
     * If a NullPointerException is thrown, it will
     * return a true, meaning there IS errors.
     */
    public boolean haveErrors()
    {
        return hasError;
    }

    /**
     * Test for any exceptions that might happen during replacing phase.
     * @return False = no bugs; True = buggy
     */
    private boolean testForErrors()
    {

        if (meta_name == null || hasError)
        {
            return true;
        }

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

            String finalString = getFinal_PatternMap();
            finalString = finalString.replaceAll(";","");

            finalString = finalString.replaceAll("<str>",strMap);

            for (String s : getFinal_DirectReplacers().keySet())
                finalString = finalString.replaceAll(s,getFinal_DirectReplacers().get(s));

        }
        catch(Exception e)
        {
            return true;
        }
        return false;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return this.meta_name;
    }

    public String getDesc()
    {
        return this.meta_desc;
    }

    public String getPerm()
    {
        return meta_perm;
    }

    public String getCursorNode()
    {
        return func_cursor;
    }

    public String getTargetNode()
    {
        return func_target;
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
