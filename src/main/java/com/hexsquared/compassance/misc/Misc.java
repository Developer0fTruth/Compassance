package com.hexsquared.compassance.misc;

import com.hexsquared.compassance.Compassance;

import java.util.logging.Level;

public class Misc
{

    public static String formatColorString(String s)
    {
        return s.replace('&','\u00A7');
    }

    public static void logHandle(Level l, String msg)
    {
        String note = "|+| ";
        if(l == Level.SEVERE)
        {
            note = "!#! ";
        }
        Compassance.getInstance().getLogger().log(l,note+msg);
    }
}
