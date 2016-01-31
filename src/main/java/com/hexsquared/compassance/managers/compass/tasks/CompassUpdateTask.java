package com.hexsquared.compassance.managers.compass.tasks;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.managers.compass.CompassStringGenerator;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import com.hexsquared.compassance.managers.themes.Theme;
import com.hexsquared.compassance.misc.ActionBarUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.hexsquared.compassance.Compassance.*;

public class CompassUpdateTask
{
    private boolean running;

    private Player p;
    private int taskId;
    private String theme;

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
                    final Theme th = Compassance.getThemeManager().getTheme(theme);

                    if(th == null)
                    {
                        p.sendMessage("Error! Theme doesn't exist!");
                        getConfigManager().getPlayerSettings().set(String.format(PlayerSettings.THEME_SELECTED, p.getPlayer().getUniqueId()), getThemeManager().getDefaultID());
                        getCompassTaskManager().refresh(p);
                        return;
                    }

                    CompassStringGenerator gen = new CompassStringGenerator(th, p.getLocation().getYaw(), false);
                    ActionBarUtil.sendActionBar(p, gen.getString());
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
