package com.hexsquared.compassance.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.hexsquared.compassance.Compassance.getCompassTaskManager;
import static com.hexsquared.compassance.Compassance.getInstance;
import static org.bukkit.Bukkit.getPluginManager;

public class PlayerQuitListener implements Listener
{
    public PlayerQuitListener()
    {
        getPluginManager().registerEvents(this, getInstance());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        getCompassTaskManager().endTask(e.getPlayer());
    }
}
