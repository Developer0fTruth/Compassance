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

import static com.hexsquared.compassance.Compassance.instance;

public class CompassUpdateTask
{
    private boolean running;

    private Player p;
    private int taskId;

    private String theme;
    private boolean cursor;
    private boolean alwaysOn;

    private double yaw;

    /**
     * Create a new task for player p.
     * Values will be handled.
     *
     * @param p Player
     */
    public CompassUpdateTask(Player p)
    {
        this.p = p;
        this.theme = instance.configManager.getPlayerSettings().getString(String.format(PlayerSettings.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()));
        this.cursor = instance.configManager.getPlayerSettings().getBoolean(String.format(PlayerSettings.SETTING_CURSOR, p.getPlayer().getUniqueId().toString()));
        this.alwaysOn = instance.configManager.getPlayerSettings().getBoolean(String.format(PlayerSettings.SETTING_ALWAYSON, p.getPlayer().getUniqueId().toString()));


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
        if (!running)
        {
            running = true;

            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Compassance.instance, new Runnable()
            {
                @Override
                public void run()
                {
                    if (!alwaysOn && yaw == p.getLocation().getYaw())
                    {
                        return;
                    }

                    final Theme th = Compassance.instance.themeManager.getTheme(theme);

                    if (th == null)
                    {
                        p.sendMessage(Misc.formatColor("&a&lCOMPASS &8» &cYour selected theme doesn't exist. Switching to default."));
                        instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId()), instance.themeManager.getDefaultID());
                        instance.compassTaskManager.refresh(p);
                        return;
                    }

                    if (!Misc.permHandle(p, th.getPerm(), true))
                    {
                        if (th.getId().equalsIgnoreCase(instance.themeManager.getDefaultID()))
                        {
                            p.sendMessage(Misc.formatColor("&a&lCOMPASS &8» &cYou don't have permission for default theme. Toggling off compass."));
                            instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()), false);
                            instance.compassTaskManager.refresh(p);
                            ActionBarUtil.sendActionBar(p, "");
                        }
                        else
                        {
                            p.sendMessage(Misc.formatColor("&a&lCOMPASS &8» &cYou don't have permission for this theme. Switching to default."));
                            instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId()), instance.themeManager.getDefaultID());
                            instance.compassTaskManager.refresh(p);
                        }
                    }

                    CompassStringGenerator gen;

                    TrackedTarget target = instance.trackingManager.getTargetOf(p);
                    if (target != null && target.getLocation() != null)
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
