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

    /**
     * Creates a new task for this player.
     *
     * @param p Player
     */
    public void newTask(Player p)
    {
        if (!tasks.containsKey(p))
        {
            Utils.updateProfile(p);
            CUpdateTask updateTask = new CUpdateTask(p);
            tasks.put(p, updateTask);
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
            CUpdateTask instance = tasks.get(p);
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
            CUpdateTask instance = tasks.get(p);
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
        for (Player e : Main.instance.getServer().getOnlinePlayers())
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
