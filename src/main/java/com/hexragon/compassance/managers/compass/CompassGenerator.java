package com.hexragon.compassance.managers.compass;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CompassGenerator
{
    private final Theme theme;

    public CompassGenerator(Theme t)
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
    public String getString(GeneratorInfo gi)
    {
        double yaw = gi.yaw;
        Location l1 = gi.l1;
        Location l2 = gi.l2;
        boolean cursor = gi.cursor;

        try
        {
            yaw = 360.0d + yaw;

            if (yaw > 360) yaw -= 360.0d;

            double ratio = yaw / 360.0d;

            StringBuilder strBuild = new StringBuilder();

            if (theme.getStringMapFull() != null)
            {
                String[] arr = theme.getStringMapArray();
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

                            if (cursor)
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

            Date d = new Date();

            finalString = finalString.replaceAll("%theme-id%", theme.id)
                    .replaceAll("%theme%", theme.meta.name)
            ;

            // TIME
            //Day of Week ex. Tues, Monday
            finalString = finalString.replaceAll("%dow%", new SimpleDateFormat("EEEE").format(d))
                    .replaceAll("%a/dow%", new SimpleDateFormat("E").format(d))

                    //Date
                    .replaceAll("%month%", new SimpleDateFormat("MMMM").format(d))
                    .replaceAll("%a/month%", new SimpleDateFormat("MMM").format(d))
                    .replaceAll("%n/month%", new SimpleDateFormat("M").format(d))
                    .replaceAll("%day%", new SimpleDateFormat("d").format(d))

                    //Time
                    .replaceAll("%time-zone%", new SimpleDateFormat("z").format(d))
                    .replaceAll("%marker%", new SimpleDateFormat("a").format(d))
                    .replaceAll("%second%", new SimpleDateFormat("ss").format(d))
                    .replaceAll("%minute%", new SimpleDateFormat("mm").format(d))
                    .replaceAll("%hour%", new SimpleDateFormat("h").format(d))
            ;

            // LOCATION
            Location loc = gi.p.getLocation();
            finalString = finalString.replaceAll("%x%", String.valueOf(loc.getBlockX()))
                    .replaceAll("%y%", String.valueOf(loc.getBlockY()))
                    .replaceAll("%z%", String.valueOf(loc.getBlockZ()))
            ;

            // YAW
            finalString = finalString.replaceAll("%yaw%", String.format("%.2f", yaw));

            if (Main.placeholderAPIExist) finalString = PlaceholderAPI.setPlaceholders(gi.p, finalString);

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

        public final Location l1;
        public final Location l2;

        /**
         * @param l1 Original location.
         * @param l2 Targetted location.
         * @param y  Yaw rotational value.
         * @param c  Use a cursor.
         */
        public GeneratorInfo(Player p, Location l1, Location l2, double y, boolean c)
        {
            this.p = p;
            this.yaw = y;
            this.l1 = l1;
            this.l2 = l2;
            this.cursor = c;
        }
    }
}
