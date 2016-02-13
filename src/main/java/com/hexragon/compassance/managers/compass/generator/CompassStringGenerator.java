package com.hexragon.compassance.managers.compass.generator;

import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.misc.Misc;
import org.bukkit.Location;

import java.util.HashMap;

public class CompassStringGenerator
{
    private Theme theme;
    private double yaw;
    private boolean cursor;

    private Location l1;
    private Location l2;

    /**
     * @param l1 Original location.
     * @param l2 Targetted location.
     * @param t  Theme string, referenced from ThemeManager.
     * @param y  Yaw rotational value.
     * @param c  Use a cursor.
     */
    public CompassStringGenerator(Location l1, Location l2, Theme t, double y, boolean c)
    {
        this.theme = t;
        this.yaw = y;
        this.l1 = l1;
        this.l2 = l2;
        this.cursor = c;
    }

    /**
     * @return Returns generated string from the created instance.
     *
     * NOTE: If any exceptions is caught and affects the function
     *       dramatically, this will automatically return an
     *       error message.
     *       If any exceptions is caught during replacement,
     *       it will just skip that value.
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
                                    String target = theme.getTargetNode();
                                    if (target != null)
                                    {
                                        target = target.replaceAll("<str>", appending).replaceAll(";", "");
                                        appending = target;
                                    }
                                }

                            }

                            if (cursor)
                            {
                                if (i == (((length / 2) + 2) / 2))
                                {
                                    String cursor = theme.getCursorNode();
                                    if (cursor != null)
                                    {
                                        cursor = cursor.replaceAll("<str>", appending).replaceAll(";", "");
                                        appending = cursor;
                                    }
                                }
                            }

                            strBuild.append(appending);
                        }

                        appendRead++;
                    }
                    catch (Exception ignore) {}
                }
            }

            /*
             * PROCESSING
             */
            String processsedString = strBuild.toString();

            HashMap<String,String> directReplacers = theme.getData_DirectReplacers();
            if (directReplacers != null && directReplacers.size() != 0)
            {
                for (String s : directReplacers.keySet())
                {
                    processsedString = processsedString.replaceAll(s, directReplacers.get(s));
                }
            }

            HashMap<String,HashMap<String,String>> subPatternReplacers = theme.getData_subPatternReplacers();
            if (subPatternReplacers != null && subPatternReplacers.size() != 0)
            {
                for (String s : subPatternReplacers.keySet())
                {
                    for (String s2 : subPatternReplacers.get(s).keySet())
                    {
                        processsedString = processsedString.replaceAll(s2, subPatternReplacers.get(s).get(s2));
                    }
                }
            }

            /*
             * POST PROCESSING
             */
            String finalString = theme.getFinal_PatternMap();

            HashMap<String,String> finalPatternMap = theme.getFinal_DirectReplacers();
            if (finalPatternMap != null && finalPatternMap.size() != 0)
            {
                finalString = finalString.replaceAll(";", "");
                finalString = finalString.replaceAll("<str>", processsedString);

                for (String s : theme.getFinal_DirectReplacers().keySet())
                {
                    finalString = finalString.replaceAll(s, theme.getFinal_DirectReplacers().get(s));
                }
            }

            return Misc.formatColor(finalString);

        }
        catch (Exception e)
        {
            return "Compassance failed to parse this theme.";
        }
    }
}
