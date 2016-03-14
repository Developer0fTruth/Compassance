package com.hexragon.compassance.managers.themes;

import org.bukkit.entity.Player;

public abstract class Theme
{
    public final String id;
    public final ThemeMain main = new ThemeMain();

    public Theme(String id)
    {
        this.id = id;
    }

    public abstract void loadData();

    public abstract String getString(Player p);

    public abstract void displayTo(Player p);

    public class ThemeMain
    {
        public String name;
        public String desc;
        public String permission;
    }
}
