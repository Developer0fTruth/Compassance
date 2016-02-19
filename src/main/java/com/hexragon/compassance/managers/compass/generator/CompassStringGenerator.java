package com.hexragon.compassance.managers.compass.generator;

import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.misc.Utils;
import org.bukkit.Location;

import java.util.HashMap;

public class CompassStringGenerator
{
    private final GeneratorInfo gi;
    private final Theme theme;
    private final boolean cursor;

    private final Location l1;
    private final Location l2;

    private double yaw;

    public CompassStringGenerator(GeneratorInfo gi)
    {
        this.gi = gi;

        this.theme = gi.theme;
        this.yaw = gi.yaw;
        this.l1 = gi.l1;
        this.l2 = gi.l2;
        this.cursor = gi.cursor;
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
    public String getString()
    {
        try
        {
            yaw = 360 + yaw;

            if (yaw > 360) yaw -= 360;

            double ratio = yaw / 360;

            StringBuilder strBuild = new StringBuilder();

            if (theme.getStringMapFull() != null)
            {
                String[] arr = theme.getStringMapArray();
                int length = arr.length;

                double shift = length / 4;
                int appendRead = (int) Math.round((length * ratio) - shift);

                double step = 360 / length;

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

                            if (l1 != null && l2 != null)
                            {
                                double angle = (Math.atan2(l1.getX() - l2.getX(), l1.getZ() - l2.getZ()));
                                angle = (-(angle / Math.PI) * 360) / 2 + 180;

                                int num1 = num + 1;
                                if (num1 >= length) num1 -= length;
                                else if (num1 < 0) num1 += length;

                                if (angle >= step * num && angle <= step * num1)
                                {
                                    String target = theme.func.target;
                                    if (target != null)
                                    {
                                        target = target.replaceAll("%str%", appending).replaceAll(";", "");
                                        appending = target;
                                    }
                                }

                            }

                            if (cursor)
                            {
                                if (i == (((length / 2) + 2) / 2))
                                {
                                    String cursor = theme.func.cursor;
                                    if (cursor != null)
                                    {
                                        cursor = cursor.replaceAll("%str%", appending).replaceAll(";", "");
                                        appending = cursor;
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

            if (finalPatternReplacers != null && finalPatternReplacers.size() != 0)
            {
                finalString = finalString.replaceAll("%str%", processsedString);
                finalString = finalString.replaceAll(";", "");

                for (String s : theme.post.replacers.keySet())
                {
                    try
                    {
                        finalString = finalString.replaceAll(s, theme.post.replacers.get(s));
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

            finalString = Utils.fmtRefs(gi, finalString);

            return Utils.fmtClr(finalString);

        }
        catch (Exception e)
        {
            return "Compassance failed to parse this theme.";
        }
    }
}
