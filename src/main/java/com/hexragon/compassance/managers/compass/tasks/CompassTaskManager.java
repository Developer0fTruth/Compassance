package com.hexragon.compassance.managers.compass.tasks;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.files.configs.PlayerConfig;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CompassTaskManager
{
    private HashMap<Player, CompassUpdateTask> tasks;

    public CompassTaskManager()
    {
        tasks = new HashMap<>();
    }

    /**
     * Creates a new task for this player.
     *
     * @param p Player
     */
    public void newTask(Player p)
    {
        if (!tasks.containsKey(p))
        {
            PlayerConfig.updateProfile(p);
            CompassUpdateTask updateTask = new CompassUpdateTask(p);
            tasks.put(p, updateTask);

            if (Compassance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_ENABLE, p.getUniqueId().toString())))
            {
                startTask(p);
            }
        }
    }

    /**
     * Start the task for this player.
     *
     * @param p Player
     */
    public void startTask(Player p)
    {
        if (tasks.containsKey(p))
        {
            CompassUpdateTask instance = tasks.get(p);
            if (!instance.isActive())
            {
                instance.start();
            }
        }
    }

    /**
     * Stop the task for this player.
     *
     * @param p Player
     */
    public void stopTask(Player p)
    {
        if (tasks.containsKey(p))
        {
            CompassUpdateTask instance = tasks.get(p);
            if (instance.isActive())
            {
                instance.stop();
            }
        }
    }

    /**
     * Delete the task for this player.
     *
     * @param p Player
     */
    public void endTask(Player p)
    {
        if (tasks.containsKey(p))
        {
            stopTask(p);
            tasks.remove(p);
        }
    }

    /**
     * Creates a new task for all online players.
     */
    public void newTaskAll()
    {
        for (Player e : Compassance.instance.getServer().getOnlinePlayers())
        {
            newTask(e);
        }
    }

    /**
     * Stop all tasks registered.
     */
    public void stopTaskAll()
    {
        for (Player e : tasks.keySet())
        {
            stopTask(e);
        }
    }

    /**
     * Stop and delete all registered tasks.
     */
    public void endTaskAll()
    {
        stopTaskAll();
        tasks.clear();
    }

    /**
     * Refresh all tasks. (Recreate all tasks).
     */
    public void refreshAll()
    {
        endTaskAll();
        newTaskAll();
    }


    /**
     * Recreate task for the player.
     *
     * @param p Player
     */
    public void refresh(Player p)
    {
        if (tasks.containsKey(p))
        {
            endTask(p);
        }
        newTask(p);
    }

}
