package com.hexragon.compassance.listeners;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.files.configs.PlayerConfig;
import org.bukkit.entity.Player;
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
        Player p = e.getPlayer();

        if (!Compassance.mainConfig.config.getBoolean(MainConfig.USE_TRACKING))
        {
            Compassance.playerConfig.config.set(String.format(PlayerConfig.SETTING_TRACKING, p.getUniqueId().toString()), false);
        }
        Compassance.compassTaskManager.newTask(p);
    }
}
