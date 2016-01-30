package com.hexsquared.compassance.managers.compass;

import com.hexsquared.compassance.managers.themes.Theme;
import com.hexsquared.compassance.misc.Misc;

public class CompassStringGenerator
{

    private Theme theme;
    private double yaw;
    //private boolean cursor;

    public CompassStringGenerator(Theme t, double y, boolean c)
    {
        this.theme = t;
        this.yaw = y;
        //this.cursor = c;
    }

    public String getString()
    {
        yaw = 360 + yaw;
        double ratio = yaw / 360;

        String[] arr = theme.getStringMapArray();
        int length = arr.length;

        double shift = length / 4;
        int appendRead = (int) Math.round((length * ratio) - shift);

        StringBuilder strBuild = new StringBuilder();

        for(int i = 0;i < (length / 2) + 3;i++)
        {
            int num;
            if(appendRead < 0)
            {
                num = length + appendRead;
            }
            else if(appendRead >= length)
            {
                num = appendRead - length;
            }
            else
            {
                num = appendRead;
            }

            num--;

            if(num >= length)
            {
                num = num - length;
            }
            else if (num < 0)
            {
                num = num + length;
            }



            if(arr[num] != null)
            {
//                if(cursor)
//                {
//                    if(i == (((length / 2) + 2) / 2)+1)
//                    {
//
//                    }
//                }
                strBuild.append(arr[num]);
            }

            appendRead++;
        }

        String finalString = strBuild.toString();

        for(String s : theme.getPattern_DirectReplacers().keySet())
        {
            finalString = finalString.replaceAll(s,theme.getPattern_DirectReplacers().get(s));
        }

        for(String s : theme.getPattern_subPatternMap().keySet())
        {
            for(String s2 : theme.getPattern_subPatternReplacers().get(s).keySet())
            {
                finalString = finalString.replaceAll(s2,theme.getPattern_subPatternReplacers().get(s).get(s2));
            }
        }

        return Misc.formatColorString(finalString);
    }
}
