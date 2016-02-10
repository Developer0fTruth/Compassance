package com.hexragon.compassance.listeners;

import com.hexragon.compassance.Compassance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getPluginManager;

public class PlayerJoinListener implements Listener
{
    public PlayerJoinListener()
    {
        getPluginManager().registerEvents(this, Compassance.instance);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Compassance.instance.compassTaskManager.newTask(e.getPlayer());
    }
}
