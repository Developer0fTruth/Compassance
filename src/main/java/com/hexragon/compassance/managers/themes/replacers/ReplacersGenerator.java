package com.hexragon.compassance.managers.themes.replacers;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.configs.ConfigurationPaths;
import com.hexragon.compassance.managers.tracking.TrackedTarget;
import com.hexragon.compassance.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ReplacersGenerator
{
    private final ReplacersTheme theme;

    public ReplacersGenerator(ReplacersTheme t)
    {
        this.theme = t;
    }

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
