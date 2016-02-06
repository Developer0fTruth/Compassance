package com.hexsquared.compassance.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.hexsquared.compassance.Compassance.instance;
import static org.bukkit.Bukkit.getPluginManager;

public class PlayerQuitListener implements Listener
{
    public PlayerQuitListener()
    {
        getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        instance.compassTaskManager.endTask(p);
        instance.trackingManager.removeTrackingFrom(p);
        instance.trackingManager.removeTrackingOf(p);
    }
}
