package com.hexragon.compassance.misc;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.managers.compass.generator.GeneratorInfo;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class Misc
{

    public static String fmtClr(String s)
    {
        return s.replace('&', '\u00A7');
    }

    public static String fmtRefs(GeneratorInfo gi, String s)
    {
        Date d = new Date();

        s = s.replaceAll("<theme-id>", gi.theme.getId())
                .replaceAll("<theme>", gi.theme.getName())
        ;

        // TIME
        //Day of Week ex. Tues, Monday
        s = s.replaceAll("<dow>", new SimpleDateFormat("EEEE").format(d))
                .replaceAll("<a/dow>", new SimpleDateFormat("E").format(d))

                //Date
                .replaceAll("<month>", new SimpleDateFormat("MMMM").format(d))
                .replaceAll("<a/month>", new SimpleDateFormat("MMM").format(d))
                .replaceAll("<n/month>", new SimpleDateFormat("M").format(d))
                .replaceAll("<day>", new SimpleDateFormat("d").format(d))

                //Time
                .replaceAll("<time-zone>", new SimpleDateFormat("z").format(d))
                .replaceAll("<marker>", new SimpleDateFormat("a").format(d))
                .replaceAll("<second>", new SimpleDateFormat("ss").format(d))
                .replaceAll("<minute>", new SimpleDateFormat("mm").format(d))
                .replaceAll("<hour>", new SimpleDateFormat("h").format(d))
        ;

        // LOCATION
        Location loc = gi.p.getLocation();
        s = s.replaceAll("<x>", String.valueOf(loc.getBlockX()))
                .replaceAll("<y>", String.valueOf(loc.getBlockY()))
                .replaceAll("<z>", String.valueOf(loc.getBlockZ()))
        ;

        // YAW
        double yaw = 360 + gi.yaw;
        if (yaw > 360) yaw -= 360;
        s = s.replaceAll("<yaw>", String.format("%.2f", yaw));

        // BALANCE
        if (Compassance.economy != null)
        {
            s = s.replaceAll("<bal>", String.format("%.2f", Compassance.economy.getBalance(gi.p)));
        }

        return s;
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
