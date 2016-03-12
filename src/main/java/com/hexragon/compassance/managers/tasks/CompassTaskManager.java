package com.hexragon.compassance.managers.tasks;

import com.hexragon.compassance.Main;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CompassTaskManager
{
    private final HashMap<Player, CompassTask> tasks;

    public CompassTaskManager()
    {
        tasks = new HashMap<>();
    }

    public void newTask(Player p)
    {
        if (!tasks.containsKey(p))
        {
            CompassTask updateTask = new CompassTask(p);
            tasks.put(p, updateTask);
        }
    }

    public void startTask(Player p)
    {
        CompassTask instance = tasks.get(p);
        if (instance != null && !instance.active)
        {
            instance.start();
        }
    }

    public void stopTask(Player p)
    {
        CompassTask instance = tasks.get(p);
        if (instance != null && instance.active)
        {
            instance.cancel();
        }
    }

    public void endTask(Player p)
    {
        if (tasks.containsKey(p))
        {
            stopTask(p);
            tasks.remove(p);
        }
    }

    public void newTaskAll()
    {
        for (Player e : Main.plugin.getServer().getOnlinePlayers())
        {
            newTask(e);
        }
    }

    public void stopTaskAll()
    {
        for (Player e : tasks.keySet()) stopTask(e);

    }

    public void endTaskAll()
    {
        stopTaskAll();
        tasks.clear();
    }

    public void refreshAll()
    {
        endTaskAll();
        newTaskAll();
    }

    public void refresh(Player p)
    {
        if (tasks.containsKey(p)) endTask(p);
        newTask(p);
    }

}
