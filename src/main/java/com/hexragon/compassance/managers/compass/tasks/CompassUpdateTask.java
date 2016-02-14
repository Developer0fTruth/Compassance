package com.hexragon.compassance.managers.compass.tasks;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.managers.compass.generator.CompassStringGenerator;
import com.hexragon.compassance.managers.compass.generator.GeneratorInfo;
import com.hexragon.compassance.managers.compass.tasks.tracking.TrackedTarget;
import com.hexragon.compassance.managers.files.configs.MainConfig;
import com.hexragon.compassance.managers.files.configs.PlayerConfig;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.misc.ActionBarUtil;
import com.hexragon.compassance.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        this.theme = Compassance.playerConfig.config.getString(String.format(PlayerConfig.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()));
        this.cursor = Compassance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_CURSOR, p.getPlayer().getUniqueId().toString()));
        this.alwaysOn = Compassance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_ALWAYSON, p.getPlayer().getUniqueId().toString()));


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

                    final Theme th = Compassance.themeManager.getTheme(theme);

                    if (th == null)
                    {
                        p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cYour selected theme doesn't exist. Switching to default."));
                        Compassance.playerConfig.config.set(String.format(PlayerConfig.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId()), Compassance.themeManager.getDefaultID());
                        Compassance.compassTaskManager.refresh(p);
                        return;
                    }

                    if ((!Misc.permHandle(p, th.getPerm(), true)) &&
                            Compassance.mainConfig.config.getBoolean(MainConfig.USE_PERMISSIONS))
                    {
                        if (th.getId().equalsIgnoreCase(Compassance.themeManager.getDefaultID()))
                        {
                            p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cYou don't have permission for default theme. Toggling off compass."));
                            Compassance.playerConfig.config.set(String.format(PlayerConfig.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()), false);
                            Compassance.compassTaskManager.refresh(p);
                            ActionBarUtil.send(p, "");
                        }
                        else
                        {
                            p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cYou don't have permission for this theme. Switching to default."));
                            Compassance.playerConfig.config.set(String.format(PlayerConfig.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId()), Compassance.themeManager.getDefaultID());
                            Compassance.compassTaskManager.refresh(p);
                        }
                    }

                    CompassStringGenerator gen;

                    TrackedTarget target = Compassance.trackingManager.getTargetOf(p);
                    if (target != null && target.getLocation() != null)
                    {
                        GeneratorInfo gi = new GeneratorInfo(p, p.getLocation(), target.getLocation(), th, p.getLocation().getYaw(), cursor);
                        gen = new CompassStringGenerator(gi);
                    }
                    else
                    {
                        GeneratorInfo gi = new GeneratorInfo(p, null, null, th, p.getLocation().getYaw(), cursor);
                        gen = new CompassStringGenerator(gi);
                    }
                    if (gen.getString() != null) ActionBarUtil.send(p, gen.getString());

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
