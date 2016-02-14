package com.hexragon.compassance.listeners;

import com.hexragon.compassance.Compassance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.bukkit.Bukkit.getPluginManager;

public class PlayerQuitListener implements Listener
{
    public PlayerQuitListener()
    {
        getPluginManager().registerEvents(this, Compassance.instance);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        Compassance.compassTaskManager.endTask(p);
        Compassance.trackingManager.removeTrackingFrom(p);
        Compassance.trackingManager.removeTrackingOf(p);
    }
}
