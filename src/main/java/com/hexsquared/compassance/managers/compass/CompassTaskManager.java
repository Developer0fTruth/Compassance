package com.hexsquared.compassance.managers.compass;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.managers.compass.tasks.CompassUpdateTask;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static com.hexsquared.compassance.Compassance.getConfigManager;

public class CompassTaskManager
{
    HashMap<Player,CompassUpdateTask> tasks;

    public CompassTaskManager()
    {
        tasks = new HashMap<>();
    }

    public void newTask(Player p)
    {
        if (!tasks.containsKey(p))
        {
            CompassUpdateTask updateTask = new CompassUpdateTask(p);
            tasks.put(p, updateTask);
            PlayerSettings.updateProfile(p);
            /*DEBUG*/ Compassance.getInstance().getServer().broadcastMessage("created new task");

            if(getConfigManager().getPlayerSettings().getBoolean(String.format(PlayerSettings.COMPASS_ENABLE, p.getUniqueId().toString())))
            {
                startTask(p);
            }
        }
    }

    public void startTask(Player p)
    {
        if(tasks.containsKey(p))
        {
            CompassUpdateTask instance = tasks.get(p);
            if(!instance.isActive())
            {
                instance.start();
                /*DEBUG*/ Compassance.getInstance().getServer().broadcastMessage("started task");
            }
        }
    }
    public void stopTask(Player p)
    {
        if(tasks.containsKey(p))
        {
            CompassUpdateTask instance = tasks.get(p);
            if (instance.isActive())
            {
                instance.stop();
                /*DEBUG*/ Compassance.getInstance().getServer().broadcastMessage("stopped task");
            }
        }
    }

    public void endTask(Player p)
    {
        if(tasks.containsKey(p))
        {
            stopTask(p);
            tasks.remove(p);
            /*DEBUG*/ Compassance.getInstance().getServer().broadcastMessage("deleted task");
        }
    }

    public void newTaskAll()
    {
        for (Player e : Compassance.getInstance().getServer().getOnlinePlayers())
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
        if(tasks.containsKey(p))
        {
            endTask(p);
            newTask(p);
        }
    }

}
