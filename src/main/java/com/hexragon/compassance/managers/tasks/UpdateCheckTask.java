package com.hexragon.compassance.managers.tasks;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

public class UpdateCheckTask
{
    private String version;

    private boolean running;
    private int taskId;

    public UpdateCheckTask()
    {
        PluginDescriptionFile pdf = Main.instance.getDescription();
        version = pdf.getVersion();
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

            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable()
            {
                @Override
                public void run()
                {
                    String onlineVer = Utils.onlineUpdateCheck();
                    if (!onlineVer.equals(version))
                    {
                        if (onlineVer.equals("null") || onlineVer.equals("error"))
                        {
                            Main.logger.warning("Attempted to fetch data on the latest version but encountered an error!");
                        }
                        else
                        {
                            Main.logger.warning(String.format("Compassance is out of date [latest: %s]. Update available at:", onlineVer));
                            Main.logger.warning("  https://www.spigotmc.org/resources/compassance.18327/");
                        }
                    }
                }
            }, 20 * 10L, 20L * Main.mainConfig.config.getLong(MainConfig.CHECKER_INTERVAL.path));
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
