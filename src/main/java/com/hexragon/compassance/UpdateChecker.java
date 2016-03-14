package com.hexragon.compassance;

import com.hexragon.compassance.configs.ConfigurationPaths;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class UpdateChecker extends BukkitRunnable
{
    private static String version;
    private boolean active;

    public UpdateChecker()
    {
        version = Main.plugin.getDescription().getVersion();
    }

    public static boolean isLatestVersion()
    {
        return updateCheck().equals(version);
    }

    public static String updateCheck()
    {
        String urlStr = "https://raw.githubusercontent.com/Hexragon/Compassance/master/latest-version";
        String text = null;
        try
        {
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(urlStr).openStream(), Charset.defaultCharset()));
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null)
                {
                    stringBuilder.append(str);
                }
                text = stringBuilder.toString();
                reader.close();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            if (text != null)
            {
                return text;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "error";
        }
        return "null";
    }

    public void start()
    {
        if (!active)
        {
            active = true;
            this.runTaskTimer(Main.plugin, 20 * 10L, 20L * Main.mainConfig.config.getLong(ConfigurationPaths.MainConfig.CHECKER_INTERVAL.path));
        }
    }

    @Override
    public void cancel()
    {
        super.cancel();
        active = false;
    }

    @Override
    public void run()
    {
        String onlineVer = updateCheck();
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
        else
        {
            Main.logger.info(String.format("Running the latest version of Compassance %s.", version));
            cancel();
        }
    }
}
