package com.hexsquared.compassance.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.hexsquared.compassance.Compassance.getCompassTaskManager;
import static com.hexsquared.compassance.Compassance.getInstance;
import static org.bukkit.Bukkit.getPluginManager;

public class PlayerJoinListener implements Listener
{
    public PlayerJoinListener()
    {
        getPluginManager().registerEvents(this, getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        getCompassTaskManager().newTask(e.getPlayer());
    }
}
