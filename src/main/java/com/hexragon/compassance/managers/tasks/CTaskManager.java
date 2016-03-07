package com.hexragon.compassance.managers.tasks;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CTaskManager
{
    private final HashMap<Player, CUpdateTask> tasks;

    public CTaskManager()
    {
        tasks = new HashMap<>();
    }

    public void newTask(Player p)
    {
        if (!tasks.containsKey(p))
        {
            Utils.updateProfile(p);
            CUpdateTask updateTask = new CUpdateTask(p);
            tasks.put(p, updateTask);
        }
    }

    public void startTask(Player p)
    {
        if (tasks.containsKey(p))
        {
            CUpdateTask instance = tasks.get(p);
            if (!instance.isActive())
            {
                instance.start();
            }
        }
    }

    public void stopTask(Player p)
    {
        if (tasks.containsKey(p))
        {
            CUpdateTask instance = tasks.get(p);
            if (instance.isActive())
            {
                instance.stop();
            }
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
        for (Player e : tasks.keySet())
        {
            stopTask(e);
        }
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
        if (tasks.containsKey(p))
        {
            endTask(p);
        }
        newTask(p);
    }

}
