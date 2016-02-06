package com.hexsquared.compassance.misc;

import com.hexsquared.compassance.Compassance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

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
        if (l == Level.SEVERE)
        {
            note = "!#! ";
        }
        Compassance.instance.getLogger().log(l, note + msg);
    }

    public static boolean permHandle(Player p, String perm, boolean ifPermIsNull)
    {
        return perm != null && p.hasPermission(perm) || perm == null && ifPermIsNull;
    }

    public static ItemStack getEgg(Entity e)
    {
        return new SpawnEgg(e.getType()).toItemStack();
    }
}
