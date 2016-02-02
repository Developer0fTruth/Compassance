package com.hexsquared.compassance.managers.compass.tasks;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.managers.compass.generator.CompassStringGenerator;
import com.hexsquared.compassance.managers.compass.tasks.tracking.TrackedTarget;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import com.hexsquared.compassance.managers.themes.Theme;
import com.hexsquared.compassance.misc.ActionBarUtil;
import com.hexsquared.compassance.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.hexsquared.compassance.Compassance.*;

public class CompassUpdateTask
{
    private boolean running;

    private Player p;
    private int taskId;

    private String theme;
    private boolean cursor;
    private boolean alwaysOn;


    private TrackedTarget target;


    private double yaw;

    /**
     * Create a new task for player p.
     * Values will be handled.
     * @param p Player
     */
    public CompassUpdateTask(Player p)
    {
        this.p = p;
        this.theme = getConfigManager().getPlayerSettings().getString(String.format(PlayerSettings.THEME_SELECTED, p.getPlayer().getUniqueId().toString()));
        this.cursor = getConfigManager().getPlayerSettings().getBoolean(String.format(PlayerSettings.COMPASS_CURSOR, p.getPlayer().getUniqueId().toString()));
        this.alwaysOn = getConfigManager().getPlayerSettings().getBoolean(String.format(PlayerSettings.COMPASS_ALWAYSON, p.getPlayer().getUniqueId().toString()));
        this.target = getTrackingManager().getTargetOf(p);

        this.running = false;
    }

    /**
     * @return Current state of task.
     */
    public boolean isActive()
    {
        return running;
    }

    /**
     * Start the task.
     */
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
                    if(!alwaysOn && yaw == p.getLocation().getYaw())
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

                    CompassStringGenerator gen;
                    if(target != null)
                    {
                        gen = new CompassStringGenerator(p.getLocation(), target.getLocation(), th, p.getLocation().getYaw(), cursor);
                    }
                    else
                    {
                        gen = new CompassStringGenerator(th, p.getLocation().getYaw(), cursor);
                    }
                    ActionBarUtil.sendActionBar(p, gen.getString());

                    yaw = p.getLocation().getYaw();

                }
            }, 0L, 2L);
        }
    }

    /**
     * Cancel the task.
     */
    public void stop()
    {
        running = false;
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
