package com.hexragon.compassance.managers.tasks;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.configs.ConfigurationPaths;
import com.hexragon.compassance.managers.compass.CompassGenerator;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.utils.ActionBar;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


class CompassTask extends BukkitRunnable
{
    private final Player p;
    private final Theme th;
    public boolean active;
    private boolean cursor;
    private boolean alwaysOn;
    private double yaw;

    /**
     * Create a new task for player p.
     * Values will be handled upon start.
     *
     * @param p Player
     */
    public CompassTask(Player p)
    {
        this.p = p;
        Utils.updateProfile(p);

        cursor = Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_CURSOR.format(p.getPlayer().getUniqueId().toString()));
        alwaysOn = Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_ALWAYSON.format(p.getPlayer().getUniqueId().toString()));
        th = Main.themeManager.getTheme(Main.playerConfig.config.getString(ConfigurationPaths.PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString())));

        if (Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_ENABLE.format(p.getUniqueId().toString())))
        {
            start();
        }
    }

    public void start()
    {
        if (!active)
        {
            active = true;
            runTaskTimer(Main.plugin, 0L, 2L);
        }
    }

    @Override
    public void run()
    {
        if (!alwaysOn && yaw == p.getLocation().getYaw())
        {
            return;
        }

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
        gi = new CompassGenerator.GeneratorInfo(p, Main.trackingManager.getTargetsOf(p), cursor);

        if (th.getGenerator().getString(gi) != null)
        {
            ActionBar.send(p, th.getGenerator().getString(gi));
        }

        yaw = p.getLocation().getYaw();
    }

    @Override
    public synchronized void cancel() throws IllegalStateException
    {
        super.cancel();
        active = false;
    }
}
