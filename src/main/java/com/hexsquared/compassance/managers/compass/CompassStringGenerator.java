package com.hexsquared.compassance.managers.compass;

import com.hexsquared.compassance.managers.themes.Theme;
import com.hexsquared.compassance.misc.Misc;
import org.bukkit.Location;

public class CompassStringGenerator
{

    private Theme theme;
    private double yaw;
    //private boolean cursor;

    private Location l1;
    private Location l2;

    public CompassStringGenerator(Theme t, double y, boolean c)
    {
        this.theme = t;
        this.yaw = y;
        //this.cursor = c;
    }

    public CompassStringGenerator(Location l1, Location l2, Theme t, double y, boolean c)
    {
        this.theme = t;
        this.yaw = y;
        this.l1 = l1;
        this.l2 = l2;
    }

    public String getString()
    {
        yaw = 360 + yaw;

        if(yaw > 360) yaw -= 360;


        double ratio = yaw / 360;

        String[] arr = theme.getStringMapArray();
        int length = arr.length;

        double shift = length / 4;
        int appendRead = (int) Math.round((length * ratio) - shift);

        double step = 360 / length;

        StringBuilder strBuild = new StringBuilder();

        for(int i = 0;i < (length / 2) + 3;i++)
        {
            int num;

            if(appendRead < 0)
                num = length + appendRead;
            else if(appendRead >= length)
                num = appendRead - length;
            else
                num = appendRead;

            num--;

            if(num >= length)
                num = num - length;
            else if (num < 0)
                num = num + length;

            if(arr[num] != null)
            {
                String appending = arr[num];
//                if(cursor)
//                {
//                    if(i == (((length / 2) + 2) / 2)+1)
//                    {
//
//                    }
//                }
                if(l1 != null && l2 != null)
                {
                    float angle = (float) (Math.atan2(l1.getX()-l2.getX(), l1.getZ()-l2.getZ()));
                    angle = (float) (-(angle / Math.PI) * 360) / 2 + 180;

                    if (angle >= step * num && angle <= step * (num + 1))
                    {
                        appending = "&f[+]";
                    }
                }
                strBuild.append(appending);
            }

            appendRead++;
        }

        // PROCESSING

        String processsedString = strBuild.toString();

        for(String s : theme.getData_DirectReplacers().keySet())
            processsedString = processsedString.replaceAll(s,theme.getData_DirectReplacers().get(s));


        for(String s : theme.getData_subPatternMap().keySet())
            for(String s2 : theme.getData_subPatternReplacers().get(s).keySet())
                processsedString = processsedString.replaceAll(s2,theme.getData_subPatternReplacers().get(s).get(s2));


        // POST PROCESSING

        String finalString = theme.getFinal_PatternMap();
        finalString = finalString.replaceAll(";","");

        finalString = finalString.replaceAll("<str>",processsedString);

        for (String s : theme.getFinal_DirectReplacers().keySet())
            finalString = finalString.replaceAll(s,theme.getFinal_DirectReplacers().get(s));

        return Misc.formatColor(finalString);
    }
}
