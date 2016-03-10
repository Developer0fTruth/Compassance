package com.hexragon.compassance.managers.tasks;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.configs.ConfigurationPaths;
import com.hexragon.compassance.managers.compass.CompassGenerator;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.utils.ActionBar;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


class CUpdateTask
{
    private final Player p;
    private boolean running;
    private int taskId;

    /**
     * Create a new task for player p.
     * Values will be handled upon start.
     *
     * @param p Player
     */
    public CUpdateTask(Player p)
    {
        this.p = p;
        this.running = false;

        if (Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_ENABLE.format(p.getUniqueId().toString())))
        {
            start();
        }
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
        class TaskVariables
        {
            private final String id = Main.playerConfig.config.getString(ConfigurationPaths.PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString()));
            private final boolean cursor = Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()));
            private final boolean alwaysOn = Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_ALWAYSON.format(p.getPlayer().getUniqueId().toString()));
            private double yaw;
        }
        final TaskVariables thisTask = new TaskVariables();

        if (!running)
        {
            running = true;

            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    if (!thisTask.alwaysOn && thisTask.yaw == p.getLocation().getYaw())
                    {
                        return;
                    }

                    final Theme th = Main.themeManager.getTheme(thisTask.id);

                    if (th == null)
                    {
                        p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cYour selected theme doesn't exist. Switching to default."));
                        Main.playerConfig.config.set(ConfigurationPaths.PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()), Main.themeManager.getDefaultID());
                        Main.taskManager.refresh(p);
                        return;
                    }

                    if ((!Utils.permHandle(p, th.meta.permission, true)) &&
                            Main.mainConfig.config.getBoolean(ConfigurationPaths.MainConfig.USE_PERMISSIONS.path))
                    {
                        if (th.id.equalsIgnoreCase(Main.themeManager.getDefaultID()))
                        {
                            p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cYou don't have permission for default theme. Toggling off compass."));
                            Main.playerConfig.config.set(ConfigurationPaths.PlayerConfig.SETTING_ENABLE.format(p.getPlayer().getUniqueId().toString()), false);
                            Main.taskManager.refresh(p);
                            ActionBar.send(p, "");
                        }
                        else
                        {
                            p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cYou don't have permission for this theme. Switching to default."));
                            Main.playerConfig.config.set(ConfigurationPaths.PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()), Main.themeManager.getDefaultID());
                            Main.taskManager.refresh(p);
                        }
                    }

                    CompassGenerator.GeneratorInfo gi;
                    gi = new CompassGenerator.GeneratorInfo(p, Main.trackingManager.getTargetsOf(p), p.getLocation().getYaw(), thisTask.cursor);

                    if (th.getGenerator().getString(gi) != null)
                    {
                            ActionBar.send(p, th.getGenerator().getString(gi));
                    }

                    thisTask.yaw = p.getLocation().getYaw();

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
