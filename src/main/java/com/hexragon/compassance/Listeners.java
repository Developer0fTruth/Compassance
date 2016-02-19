package com.hexragon.compassance;

import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.files.configs.PlayerConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.bukkit.Bukkit.getPluginManager;

public class Listeners implements Listener
{
    public Listeners()
    {
        getPluginManager().registerEvents(this, Compassance.instance);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();

        if (!Compassance.mainConfig.config.getBoolean(MainConfig.USE_TRACKING.path))
        {
            Compassance.playerConfig.config.set(PlayerConfig.SETTING_TRACKING.format(p.getUniqueId().toString()), false);
        }
        Compassance.compassTaskManager.newTask(p);
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
