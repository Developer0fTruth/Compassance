package com.hexsquared.compassance.commands;

import com.hexsquared.compassance.Compassance;
import com.hexsquared.compassance.managers.compass.generator.CompassStringGenerator;
import com.hexsquared.compassance.managers.settings.paths.PlayerSettings;
import com.hexsquared.compassance.managers.themes.Theme;
import com.hexsquared.compassance.misc.ActionBarUtil;
import com.hexsquared.compassance.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.hexsquared.compassance.Compassance.instance;

public class TestCommand implements CommandExecutor
{
    public TestCommand()
    {
        instance.getCommand("test").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("data"))
        {
            Theme theme = instance.themeManager.getTheme(args[1]);

            if (theme == null)
            {
                return true;
            }

            sender.sendMessage("Displaying data for theme:\n" + theme.getName());

            sender.sendMessage("Name: " + theme.getName());
            sender.sendMessage(Misc.formatColor("Desc: " + theme.getDesc()));
            sender.sendMessage("PatternMap: " + theme.getData_main_PatternMap());

            sender.sendMessage("DirectReplacers:");
            for (String s : theme.getData_DirectReplacers().keySet())
            {
                sender.sendMessage(Misc.formatColor("   " + s + " : " + theme.getData_DirectReplacers().get(s)));
            }
            sender.sendMessage("SubPatternMap:");
            for (String s : theme.getData_subPatternMap().keySet())
            {
                sender.sendMessage("   " + s + " : " + theme.getData_subPatternMap().get(s));
                for (String s2 : theme.getData_subPatternReplacers().get(s).keySet())
                {
                    sender.sendMessage("         " + s2 + " : " + theme.getData_subPatternReplacers().get(s).get(s2));
                }
            }


            sender.sendMessage("Final:");
            sender.sendMessage(" PatternMap: " + theme.getFinal_PatternMap());
            for (String s : theme.getFinal_DirectReplacers().keySet())
            {
                sender.sendMessage(Misc.formatColor("   " + s + " : " + theme.getFinal_DirectReplacers().get(s)));
            }

            sender.sendMessage(theme.getStringMapFull());
            sender.sendMessage(String.valueOf(theme.getStringMapArray().length));

            if (sender instanceof Player)
            {
                CompassStringGenerator gen = new CompassStringGenerator(theme, ((Player) sender).getLocation().getYaw(), false);
                ActionBarUtil.sendActionBar((Player) sender, gen.getString());
            }

            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("perm"))
        {
            Player p = (Player) sender;

            Theme theme = instance.themeManager.getTheme(args[1]);
            if (theme == null)
            {
                return true;
            }

            p.sendMessage(String.valueOf(Misc.permHandle(p, theme.getPerm(), true)));


            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("mydir"))
        {
            Player p = (Player) sender;
            p.sendMessage(String.valueOf(p.getLocation().getYaw()));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("dirto"))
        {
            Player p = (Player) sender;
            Location ploc = p.getLocation();
            Location loc = new Location(p.getWorld(), 0, 65, 0);

            float angle = (float) (Math.atan2(loc.getX() - ploc.getX(), loc.getZ() - ploc.getZ()));
            angle = (float) (-(angle / Math.PI) * 360) / 2 + 180;

            p.sendMessage(String.valueOf(angle));
            return true;
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("loc"))
        {
            Player p = (Player) sender;
            try
            {
                Double x = Double.parseDouble(args[1]);
                Double y = Double.parseDouble(args[2]);
                Double z = Double.parseDouble(args[3]);
                Compassance.instance.trackingManager.newTracking(p, new Location(p.getWorld(), x, y, z));

                instance.compassTaskManager.refresh(p);
            }
            catch (Exception e)
            {
                p.sendMessage("error");
            }
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("player"))
        {
            Player p = (Player) sender;

            String targetName = args[1];
            for (Player pl : Bukkit.getOnlinePlayers())
            {
                if (pl.getName().equalsIgnoreCase(targetName))
                {
                    Compassance.instance.trackingManager.newTracking(p, pl);
                    break;
                }
            }
            instance.compassTaskManager.refresh(p);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("toggle"))
        {
            Player p = (Player) sender;
            //String uuid = p.getUniqueId().toString();
            boolean b = instance.configManager.getPlayerSettings().getBoolean(String.format(PlayerSettings.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()));
            if (b)
            {
                instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()), false);
            }
            else
            {
                instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.SETTING_ENABLE, p.getPlayer().getUniqueId().toString()), true);
            }
            instance.compassTaskManager.refresh(p);
            ActionBarUtil.sendActionBar(p, "");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("menu"))
        {
            //sender.sendMessage(instance.configManager.getThemeConfig().getDefaults().getString("version") + " default vs have " + instance.configManager.getThemeConfig().getString("version"));
            //sender.sendMessage(Bukkit.getVersion()+" getVersion and getBukkitVersion "+Bukkit.getBukkitVersion());
            Compassance.instance.mainMenu.show((Player) sender);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("theme"))
        {
            Player p = (Player) sender;
            instance.configManager.getPlayerSettings().set(String.format(PlayerSettings.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId()), args[1]);
            instance.compassTaskManager.refresh(p);
            return true;
        }

        return false;
    }
}