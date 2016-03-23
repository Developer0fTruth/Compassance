package com.hexragon.compassance.managers.themes.replacers;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.configs.ConfigurationPaths;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.utils.ActionBar;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBarAPI;

import java.util.HashMap;

public class ReplacersTheme extends Theme
{
    public final ThemeData data = new ThemeData();
    public final ThemeFinal post = new ThemeFinal();
    public final ThemeFunctionals func = new ThemeFunctionals();

    private final String display;

    private final ReplacersGenerator generator;

    public ReplacersTheme(String id)
    {
        super(id);

        generator = new ReplacersGenerator(this);

        data.replacers = new HashMap<>();
        data.sub.pattern = new HashMap<>();
        data.sub.replacers = new HashMap<>();

        display = Main.mainConfig.config.getString("display");

        post.replacers = new HashMap<>();
    }

    @Override
    public void loadData()
    {
        main.name = Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_META_NAME.format(id));
        main.desc = Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_META_DESC.format(id));
        main.permission = Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_META_PERM.format(id));

        try
        {
            data.mainPatternMap = Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_DATA_MAIN_PATTERN_MAP.format(id));

            if (data.mainPatternMap != null)
            {
                String[] mainPatternReplacers = data.mainPatternMap.split(";");
                for (String s : mainPatternReplacers)
                {
                    /*
                     & Search in sub-pattern node.
                     */
                    if (s.startsWith("<s/") && s.endsWith(">"))
                    {
                        String subPatternMap = Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_DATA_SUBPATTERN_MAP.format(id, s));

                        String[] subPatternReplacers = subPatternMap.split(";");
                        HashMap<String, String> map = new HashMap<>();

                        for (String s2 : subPatternReplacers)
                        {
                            map.put(s2, Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_DATA_SUBPATTERN_MAP_REPLACER.format(id, s, s2)));
                        }

                        data.sub.pattern.put(s, subPatternMap);
                        data.sub.replacers.put(s, map);
                    }

                    /*
                     * Search in direct node.
                     */
                    else if (s.startsWith("<") && s.endsWith(">"))
                    {
                        String d_replacer = Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_DATA_DIRECT_REPLACER.format(id, s));
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
            post.pattern = Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_FINAL_MAIN_PATTERN_MAP.format(id));
            String[] finalPatternReplacers = post.pattern.split(";");

            for (String s : finalPatternReplacers)
            {
                if (s.startsWith("<") && s.endsWith(">"))
                {
                    String d_replacer = Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_FINAL_DIRECT_REPLACER.format(id, s));
                    post.replacers.put(s, d_replacer);
                }
            }
            func.cursor = Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_DATA_FUNC_CURSOR.format(id));
            func.target = Main.themeConfig.config.getString(ConfigurationPaths.ThemeConfig.THEME_DATA_FUNC_TARGET.format(id));
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    public String getString(Player p)
    {
        ReplacersGenerator.GeneratorInfo gi;
        gi = new ReplacersGenerator.GeneratorInfo(p, Main.trackingManager.getTargetsOf(p));
        return getGenerator().generateString(gi);
    }

    @Override
    public void displayTo(Player p)
    {
        ReplacersGenerator.GeneratorInfo gi;
        gi = new ReplacersGenerator.GeneratorInfo(p, Main.trackingManager.getTargetsOf(p));

        String s = getGenerator().generateString(gi);

        if (s != null)
        {
            switch (display)
            {
                case "action":
                case "actionbar":
                    ActionBar.send(p, s);
                    break;

                case "boss":
                case "bossbar":
                    if (ActionBar.nmsVer.contains("v1_9_")) BossBarAPI.removeAllBars(p);
                    BossBarAPI.setMessage(p, s);
                    break;

            }
        }
    }

    public String getStringMapFull()
    {
        String strMap = data.mainPatternMap;
        for (String s : data.sub.pattern.keySet())
        {
            strMap = strMap.replaceAll(s, data.sub.pattern.get(s));
        }
        return strMap;
    }

    public ReplacersGenerator getGenerator()
    {
        return generator;
    }

    public class ThemeData
    {
        public final ThemeSubData sub = new ThemeSubData();

        public String mainPatternMap;
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
