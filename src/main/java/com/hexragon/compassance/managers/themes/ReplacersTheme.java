package com.hexragon.compassance.managers.themes;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.configs.ConfigurationPaths;
import com.hexragon.compassance.managers.tracking.TrackedTarget;
import com.hexragon.compassance.utils.ActionBar;
import com.hexragon.compassance.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ReplacersTheme extends Theme
{
    public final ThemeData data = new ThemeData();
    public final ThemeFinal post = new ThemeFinal();
    public final ThemeFunctionals func = new ThemeFunctionals();

    private final Generator generator;

    public ReplacersTheme(String id)
    {
        super(id);

        generator = new Generator(this);

        data.replacers = new HashMap<>();
        data.sub.pattern = new HashMap<>();
        data.sub.replacers = new HashMap<>();

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
        Generator.GeneratorInfo gi;
        gi = new Generator.GeneratorInfo(p, Main.trackingManager.getTargetsOf(p));
        return getGenerator().generateString(gi);
    }

    @Override
    public void displayTo(Player p)
    {
        Generator.GeneratorInfo gi;
        gi = new Generator.GeneratorInfo(p, Main.trackingManager.getTargetsOf(p));
        if (getGenerator().generateString(gi) != null)
        {
            ActionBar.send(p, getGenerator().generateString(gi));
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

    public Generator getGenerator()
    {
        return generator;
    }

    public static class Generator
    {
        private final ReplacersTheme theme;

        public Generator(ReplacersTheme t)
        {
            this.theme = t;
        }

        /**
         * @return Returns generated string from the created instance.
         * <p/>
         * NOTE: If any exceptions is caught and affects the function
         * dramatically, this will automatically return an
         * error message.
         * If any exceptions is caught during replacement,
         * it will just skip that value.
         */
        public String generateString(GeneratorInfo gi)
        {
            double yaw = gi.yaw;

            try
            {
                yaw = 360.0d + yaw;

                if (yaw > 360) yaw -= 360.0d;

                double ratio = yaw / 360.0d;

                StringBuilder strBuild = new StringBuilder();

                if (theme.getStringMapFull() != null)
                {
                    String[] arr = theme.getStringMapFull().split(";");
                    int length = arr.length;

                    double shift = length / 4;
                    int appendRead = (int) Math.round((length * ratio) - shift);

                    double step = 360.0d / length;

                    for (int i = 0; i < (length / 2) + 3; i++)
                    {
                        try
                        {
                            int num;

                            if (appendRead < 0) num = length + appendRead;
                            else if (appendRead >= length) num = appendRead - length;
                            else num = appendRead;

                            num--;

                            if (num >= length) num -= length;
                            else if (num < 0) num += length;

                            if (arr[num] != null || !arr[num].isEmpty())
                            {
                                String appending = arr[num];

                                if (gi.targets != null && gi.p != null)
                                {
                                    for (int trackNum : gi.targets.keySet())
                                    {
                                        TrackedTarget target = gi.targets.get(trackNum);

                                        Location l1 = gi.p.getLocation();
                                        Location l2 = null;
                                        if (target != null) l2 = target.getLocation();

                                        if (l1 != null && l2 != null)
                                        {
                                            double angle = (Math.atan2(l1.getX() - l2.getX(), l1.getZ() - l2.getZ()));
                                            angle = (-(angle / Math.PI) * 360.0d) / 2.0d + 180.0d;

                                            int num1 = num + 1;
                                            if (num1 >= length) num1 -= length;
                                            else if (num1 < 0) num1 += length;

                                            if (angle >= step * num && angle <= step * num1)
                                            {
                                                String targetNode = theme.func.target;
                                                if (targetNode != null)
                                                {
                                                    targetNode = targetNode.replaceAll("%str%", appending).replaceAll(";", "");
                                                    appending = targetNode;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (gi.cursor)
                                {
                                    if (i == (((length / 2) + 2) / 2))
                                    {
                                        String cursorNode = theme.func.cursor;
                                        if (cursorNode != null)
                                        {
                                            cursorNode = cursorNode.replaceAll("%str%", appending).replaceAll(";", "");
                                            appending = cursorNode;
                                        }
                                    }
                                }

                                strBuild.append(appending);
                            }

                            appendRead++;
                        }
                        catch (Exception ignore)
                        {
                        }
                    }
                }

                /*
                 * PROCESSING
                 */
                String processsedString = strBuild.toString();

                HashMap<String, String> directReplacers = theme.data.replacers;
                if (directReplacers != null && directReplacers.size() != 0)
                {
                    for (String s : directReplacers.keySet())
                    {
                        try
                        {
                            processsedString = processsedString.replaceAll(s, directReplacers.get(s));
                        }
                        catch (Exception ignore)
                        {
                        }
                    }
                }

                HashMap<String, HashMap<String, String>> subPatternReplacers = theme.data.sub.replacers;
                if (subPatternReplacers != null && subPatternReplacers.size() != 0)
                {
                    for (String s : subPatternReplacers.keySet())
                    {
                        for (String s2 : subPatternReplacers.get(s).keySet())
                        {
                            try
                            {
                                processsedString = processsedString.replaceAll(s2, subPatternReplacers.get(s).get(s2));
                            }
                            catch (Exception ignore)
                            {
                            }
                        }
                    }
                }

                /*
                 * POST PROCESSING
                 */
                String finalString = theme.post.pattern;

                HashMap<String, String> finalPatternReplacers = theme.post.replacers;

                if (finalString.length() != 0)
                {
                    finalString = finalString.replaceAll("%str%", processsedString);
                    finalString = finalString.replaceAll(";", "");

                    for (String s : finalPatternReplacers.keySet())
                    {
                        try
                        {
                            finalString = finalString.replaceAll(s, finalPatternReplacers.get(s));
                        }
                        catch (Exception ignore)
                        {
                        }
                    }
                }
                else
                {
                    finalString = processsedString;
                }

                if (Main.placeholderAPIExist && gi.p != null)
                    finalString = PlaceholderAPI.setPlaceholders(gi.p, finalString);

                return Utils.fmtClr(finalString);

            }
            catch (Exception e)
            {
                return "Compassance failed to parse this theme.";
            }
        }

        public static class GeneratorInfo
        {
            public final Player p;
            public final double yaw;
            public final boolean cursor;


            public final HashMap<Integer, TrackedTarget> targets;

            public GeneratorInfo(Player p, HashMap<Integer, TrackedTarget> targets)
            {
                this.p = p;
                this.yaw = p.getLocation().getYaw();
                this.targets = targets;
                this.cursor = Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_CURSOR.format(p.getUniqueId().toString()));
            }

            public GeneratorInfo(double yaw, boolean cursor)
            {
                this.p = null;
                this.targets = null;
                this.cursor = cursor;
                this.yaw = yaw;
            }
        }
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
