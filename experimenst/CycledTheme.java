package com.hexragon.compassance.managers.themes;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.utils.ActionBar;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;

public class CycledTheme extends Theme
{
    private final ArrayList<String> frames;
    Iterator<String> iterator;
    private String currentFrame;

    public CycledTheme(String id)
    {
        super(id);

        frames = new ArrayList<String>()
        {{
            add("&nT&resting");
            add("T&ne&rsting");
            add("Te&ns&rting");
            add("Tes&nt&ring");
            add("Test&ni&rng");
            add("Testi&nn&rg");
            add("Testin&ng&r");

            add("Testi&nn&rg");
            add("Test&ni&rng");
            add("Tes&nt&ring");
            add("Te&ns&rting");
            add("T&ne&rsting");
        }};

        run();
    }

    @Override
    public void loadData()
    {
        main.name = "&7Animated Text";
        main.desc = "&7Amazing right? New API rocks!";
        main.permission = null;
    }

    @Override
    public String getString(Player p)
    {
        return Utils.fmtClr(currentFrame);
    }

    @Override
    public void displayTo(Player p)
    {
        ActionBar.send(p, Utils.fmtClr(currentFrame));
    }

    public void run()
    {
        iterator = frames.iterator();

        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (iterator.hasNext())
                {
                    currentFrame = iterator.next();
                }
                else
                {
                    iterator = frames.iterator();
                    if (iterator.hasNext())
                    {
                        currentFrame = iterator.next();
                    }
                }
            }
        };

        runnable.runTaskTimer(Main.plugin, 0L, 2L);
    }
}
