package com.hexsquared.compassance.managers.compass.tasks;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.managers.compass.CompassStringGenerator;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import com.hexsquared.compassance.managers.themes.Theme;
import com.hexsquared.compassance.misc.ActionBarUtil;
import com.hexsquared.compassance.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.hexsquared.compassance.Compassance.*;

public class CompassUpdateTask
{
    private boolean running;

    private Player p;
    private int taskId;
    private String theme;


    private double yaw;

    public CompassUpdateTask(Player p)
    {
        this.p = p;
        this.theme = getConfigManager().getPlayerSettings().getString(String.format(PlayerSettings.THEME_SELECTED, p.getPlayer().getUniqueId()));

        this.running = false;
    }

    // Returns current state of task.
    public boolean isActive()
    {
        return running;
    }

    public void start()
    {
        if(!running)
        {
            running = true;

            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Compassance.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    if(false && yaw == p.getLocation().getYaw()) // EXPERIMENTAL
                    {
                        return;
                    }

                    final Theme th = Compassance.getThemeManager().getTheme(theme);

                    if(th == null)
                    {
                        p.sendMessage("&a&lCOMPASS &8» &cYour selected theme doesn't exist. Switching to default.");
                        getConfigManager().getPlayerSettings().set(String.format(PlayerSettings.THEME_SELECTED, p.getPlayer().getUniqueId()), getThemeManager().getDefaultID());
                        getCompassTaskManager().refresh(p);
                        return;
                    }

                    if(!Misc.permHandle(p,th.getPerm(),true))
                    {
                        if(th.getId().equalsIgnoreCase(getThemeManager().getDefaultID()))
                        {
                            p.sendMessage("&a&lCOMPASS &8» &cYou don't have permission for default theme. Toggling off compass.");
                            getConfigManager().getPlayerSettings().set(String.format(PlayerSettings.COMPASS_ENABLE, p.getPlayer().getUniqueId().toString()),false);
                            getCompassTaskManager().refresh(p);
                            ActionBarUtil.sendActionBar(p,"");
                        }
                        else
                        {
                            p.sendMessage("&a&lCOMPASS &8» &cYou don't have permission for this theme. Switching to default.");
                            getConfigManager().getPlayerSettings().set(String.format(PlayerSettings.THEME_SELECTED, p.getPlayer().getUniqueId()), getThemeManager().getDefaultID());
                            getCompassTaskManager().refresh(p);
                        }
                    }

                    //CompassStringGenerator gen = new CompassStringGenerator(th, p.getLocation().getYaw(), false);
                    CompassStringGenerator gen = new CompassStringGenerator(p.getLocation(),new Location(p.getLocation().getWorld(),0,65,0),th, p.getLocation().getYaw(), false);
                    ActionBarUtil.sendActionBar(p, gen.getString());

                    yaw = p.getLocation().getYaw();

                }
            }, 0L, 2L);
        }
    }

    public void stop()
    {
        running = false;
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
