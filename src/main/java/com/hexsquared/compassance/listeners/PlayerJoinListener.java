package com.hexsquared.compassance.listeners;

import com.hexsquared.compassance.Compassance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.hexsquared.compassance.Compassance.instance;
import static org.bukkit.Bukkit.getPluginManager;

public class PlayerJoinListener implements Listener
{
    public PlayerJoinListener()
    {
        getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Compassance.instance.compassTaskManager.newTask(e.getPlayer());
    }
}
