package com.hexragon.compassance.managers.tasks;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.files.configs.PlayerConfig;
import com.hexragon.compassance.managers.compass.CompassGenerator;
import com.hexragon.compassance.managers.tasks.tracking.TrackedTarget;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.utils.ActionBar;
import com.hexragon.compassance.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;


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

        if (Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_ENABLE.format(p.getUniqueId().toString())))
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
            private final String id = Main.playerConfig.config.getString(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString()));
            private final boolean cursor = Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()));
            private final boolean alwaysOn = Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_ALWAYSON.format(p.getPlayer().getUniqueId().toString()));
            private final String position = Main.playerConfig.config.getString(PlayerConfig.SETTING_POSITION.format(p.getPlayer().getUniqueId().toString())).toLowerCase();
            private double yaw;
            private BossBar barObj;
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

                    if (Main.nmsver.startsWith("v1_9_") && thisTask.barObj != null)
                    {
                        thisTask.barObj.removePlayer(p);
                    }

                    final Theme th = Main.themeManager.getTheme(thisTask.id);

                    if (th == null)
                    {
                        p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cYour selected theme doesn't exist. Switching to default."));
                        Main.playerConfig.config.set(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()), Main.themeManager.getDefaultID());
                        Main.taskManager.refresh(p);
                        return;
                    }

                    if ((!Utils.permHandle(p, th.meta.permission, true)) &&
                            Main.mainConfig.config.getBoolean(MainConfig.USE_PERMISSIONS.path))
                    {
                        if (th.id.equalsIgnoreCase(Main.themeManager.getDefaultID()))
                        {
                            p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cYou don't have permission for default theme. Toggling off compass."));
                            Main.playerConfig.config.set(PlayerConfig.SETTING_ENABLE.format(p.getPlayer().getUniqueId().toString()), false);
                            Main.taskManager.refresh(p);
                            ActionBar.send(p, "");
                        }
                        else
                        {
                            p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cYou don't have permission for this theme. Switching to default."));
                            Main.playerConfig.config.set(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId()), Main.themeManager.getDefaultID());
                            Main.taskManager.refresh(p);
                        }
                    }

                    CompassGenerator.GeneratorInfo gi;
                    TrackedTarget target = Main.trackingManager.getTargetOf(p);
                    if (target != null && target.getLocation() != null)
                    {
                        gi = new CompassGenerator.GeneratorInfo(p, p.getLocation(), target.getLocation(), p.getLocation().getYaw(), thisTask.cursor);
                    }
                    else
                    {
                        gi = new CompassGenerator.GeneratorInfo(p, null, null, p.getLocation().getYaw(), thisTask.cursor);
                    }
                    if (th.getGenerator().getString(gi) != null)
                    {
                        if (thisTask.position.equals("actionbar"))
                        {
                            ActionBar.send(p, th.getGenerator().getString(gi));
                        }
                        else if (thisTask.position.equals("bossbar") && Main.bossbarAPIExist)
                        {
                            if (Main.nmsver.startsWith("v1_9_"))
                            {
                                thisTask.barObj = BossBarAPI.addBar(p, // The receiver of the BossBar
                                        new TextComponent(th.getGenerator().getString(gi)), // Displayed message
                                        BossBarAPI.Color.BLUE, // Color of the bar
                                        BossBarAPI.Style.NOTCHED_20, // Bar style
                                        1.0f, 10, 1);
                            }
                            else if (Main.nmsver.startsWith("v1_8_"))
                            {
                                BossBarAPI.setMessage(p, th.getGenerator().getString(gi));
                            }

                        }
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
