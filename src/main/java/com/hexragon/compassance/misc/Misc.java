package com.hexragon.compassance.misc;

import com.hexragon.compassance.Compassance;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Misc
{

    public static String formatColor(String s)
    {
        return s.replace('&', '\u00A7');
    }

    public static void logHandle(Level l, String msg)
    {
        String note = "|+| ";
        if (l == Level.SEVERE || l == Level.WARNING)
        {
            note = "!#! ";
        }
        Compassance.instance.getLogger().log(l, note + msg);
    }

    public static boolean permHandle(Player p, String perm, boolean ifPermIsNull)
    {
        return perm != null && p.hasPermission(perm) || perm == null && ifPermIsNull;
    }
}
