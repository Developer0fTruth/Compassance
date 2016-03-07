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
        getPluginManager().registerEvents(this, Main.plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();

        if (!Main.mainConfig.config.getBoolean(MainConfig.USE_TRACKING.path))
        {
            Main.playerConfig.config.set(PlayerConfig.SETTING_TRACKING.format(p.getUniqueId().toString()), false);
        }
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
