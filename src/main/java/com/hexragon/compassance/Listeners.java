package com.hexragon.compassance;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class Listeners implements Listener
{
    public Listeners()
    {
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        Main.taskManager.newTask(p);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        Main.taskManager.endTask(p);
        Main.trackingManager.removeTrackingFrom(p);
        Main.trackingManager.removeTrackingOf(p);
    }
}
