package com.hexragon.compassance.managers.compass.generator;


import com.hexragon.compassance.managers.themes.Theme;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GeneratorInfo
{
    public final Player p;
    public final Theme theme;
    public final double yaw;
    public final boolean cursor;

    public final Location l1;
    public final Location l2;

    /**
     * @param l1 Original location.
     * @param l2 Targetted location.
     * @param t  Theme string, referenced from ThemeManager.
     * @param y  Yaw rotational value.
     * @param c  Use a cursor.
     */
    public GeneratorInfo(Player p, Location l1, Location l2, Theme t, double y, boolean c)
    {
        this.p = p;
        this.theme = t;
        this.yaw = y;
        this.l1 = l1;
        this.l2 = l2;
        this.cursor = c;
    }
}
