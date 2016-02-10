package com.hexragon.compassance.managers.themes;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.managers.settings.paths.ThemeSettings;

import java.util.HashMap;

public class Theme
{

    private final String id;
    private String meta_name;
    private String meta_desc;
    private String meta_perm;

    private String func_cursor;
    private String func_target;

    private String data_main_PatternMap;
    private HashMap<String, String> data_DirectReplacers;

    private HashMap<String, String> data_subPatternMap;
    private HashMap<String, HashMap<String, String>> data_subPatternReplacers;

    private String final_PatternMap;
    private HashMap<String, String> final_DirectReplacers;

    public Theme(String id)
    {
        this.id = id;

        data_DirectReplacers = new HashMap<>();
        data_subPatternMap = new HashMap<>();
        data_subPatternReplacers = new HashMap<>();
        final_DirectReplacers = new HashMap<>();

        loadData();
    }

    /**
     * Grab all data from themes-config.yml using designated paths.
     * Associate values with a map so CompassStringGenerator can
     * generated string with styled characters.
     */
    public void loadData()
    {
        try
        {
            this.meta_name = Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_META_NAME, id));
            this.meta_desc = Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_META_DESC, id));
            this.meta_perm = Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_META_PERM, id));

            this.data_main_PatternMap = Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_MAIN_PATTERN_MAP, id));
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
                        String subPatternMap = Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_SUBPATTERN_MAP, id, s));

                        String[] subPatternReplacers = subPatternMap.split(";");
                        HashMap<String, String> map = new HashMap<>();

                        for (String s2 : subPatternReplacers)
                        {
                            map.put(s2, Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_SUBPATTERN_MAP_REPLACER, id, s, s2)));
                        }

                        data_subPatternMap.put(s, subPatternMap);
                        data_subPatternReplacers.put(s, map);
                    }

                    /*
                     * Search in direct node.
                     */
                    else
                    {
                        String d_replacer = Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_DIRECT_REPLACER, id, s));
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
            final_PatternMap = Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_FINAL_MAIN_PATTERN_MAP, id));
            String[] finalPatternReplacers = final_PatternMap.split(";");

            for (String s : finalPatternReplacers)
            {
                String d_replacer = Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_FINAL_DIRECT_REPLACER, id, s));
                final_DirectReplacers.put(s, d_replacer);
            }

            final_DirectReplacers.remove("<str>");

            func_cursor = Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_FUNC_CURSOR, id));
            func_target = Compassance.instance.configManager.getThemeConfig().getString(String.format(ThemeSettings.THEME_DATA_FUNC_TARGET, id));
        }
        catch (Exception ignored) {}
    }

    /**
     * Apply the sub-pattern mapping.
     * Returns full string with everything.
     */
    public String getStringMapFull()
    {
        String strMap = data_main_PatternMap;
        for (String s : getData_subPatternMap().keySet())
        {
            strMap = strMap.replaceAll(s, getData_subPatternMap().get(s));
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

    public HashMap<String, HashMap<String, String>> getData_subPatternReplacers()
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
