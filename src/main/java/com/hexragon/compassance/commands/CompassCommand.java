package com.hexragon.compassance.commands;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.files.configs.PlayerConfig;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CompassCommand implements CommandExecutor
{
    public CompassCommand()
    {
        Main.instance.getCommand("compass").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("You must be a player to execute this command.");
            return true;
        }

        if (args.length == 0)
        {
            Main.mainMenu.show((Player) sender);
            return true;
        }
        else if (args[0].equalsIgnoreCase("help"))
        {
            ArrayList<String> arr = new ArrayList<>();
            arr.add("&8&m--------------------------------------------------");
            arr.add("");
            arr.add("        &2&lCompassance");
            arr.add("");
            arr.add("    &f/compass open &7- &f&aOpen compass settings menu.");
            arr.add("    &f/compass theme -id &7- &f&aChange theme based on id.");
            arr.add("    &f/compass track -args... &7- &f&aTrack a target.");
            arr.add("");
            arr.add("&8&m--------------------------------------------------");

            for (String s : arr)
            {
                sender.sendMessage(Utils.fmtClr(s));
            }
            return true;
        }
        else if (args[0].equalsIgnoreCase("open"))
        {
            Main.mainMenu.show((Player) sender);
            return true;
        }
        else if (args[0].equalsIgnoreCase("theme"))
        {
            Player p = (Player) sender;

            if (args.length < 2)
            {
                p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cInsufficient amount of arguments."));
                p.sendMessage(Utils.fmtClr("&a&lUSAGE &8» &7/compass theme &ftheme-id"));
                return true;
            }

            if (Main.themeManager.getTheme(args[1]) == null)
            {
                p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cTheme ID doesn't exist."));
                return true;
            }

            p.sendMessage(Utils.fmtClr(String.format("&a&lCOMPASS &8» &7Switching your selected theme to &r%s&7.", Main.themeManager.getTheme(args[1]).meta.name)));
            Main.playerConfig.config.set(PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString()), args[1]);
            return true;
        }
        else if (args[0].equalsIgnoreCase("track") || args[0].equalsIgnoreCase("trk"))
        {
            Player p = (Player) sender;

            if (!Main.mainConfig.config.getBoolean(MainConfig.USE_TRACKING.path))
            {
                p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cTracking is globally disabled."));
                return true;
            }

            boolean b = Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_TRACKING.format(p.getUniqueId().toString()));
            if (!b)
            {
                p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cYou must enable tracking in the Compassance menu."));
                return true;
            }

            if (args.length < 2)
            {
                p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cInsufficient amount of arguments."));
                p.sendMessage(Utils.fmtClr("&a&lUSAGE &8» &7/compass trk &fpl &7or &7/compass trk &floc"));
                return true;
            }

            if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("pl"))
            {
                if (args.length != 3)
                {
                    p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cInsufficient amount of arguments."));
                    p.sendMessage(Utils.fmtClr("&a&lUSAGE &8» &7/compass trk pl &f-player"));
                    return true;
                }

                String targetName = args[2];
                for (Player pl : Bukkit.getOnlinePlayers())
                {
                    if (pl.getName().equalsIgnoreCase(targetName))
                    {
                        if (pl == p)
                        {
                            p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cYou can't track yourself."));
                            return true;
                        }

                        boolean b1 = Main.playerConfig.config.getBoolean(PlayerConfig.SETTING_TRACKING.format(pl.getUniqueId().toString()));
                        if (!b1)
                        {
                            p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cThat player must enable tracking to be able to be tracked."));
                            return true;
                        }

                        Main.trackingManager.newTracking(p, pl);
                        Main.taskManager.refresh(p);
                        p.sendMessage(Utils.fmtClr(String.format("&a&lCOMPASS &8» &7You are now tracking player &f%s&7.", pl.getName())));
                        pl.sendMessage(Utils.fmtClr(String.format("&a&lCOMPASS &8» &7You are being tracked by &f%s&7.", p.getName())));
                        return true;
                    }
                }
                p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cThe player you are attempting to track is not found."));

                return true;
            }
            else if (args[1].equalsIgnoreCase("location") || args[1].equalsIgnoreCase("loc"))
            {
                if (args.length != 5)
                {
                    p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cInsufficient amount of arguments."));
                    p.sendMessage(Utils.fmtClr("&a&lUSAGE &8» &7/compass trk loc &f-x -y -z&7."));
                    return true;
                }

                try
                {
                    Double x = Double.parseDouble(args[2]);
                    Double y = Double.parseDouble(args[3]);
                    Double z = Double.parseDouble(args[4]);
                    Main.trackingManager.newTracking(p, new Location(p.getWorld(), x, y, z));

                    Main.taskManager.refresh(p);
                    p.sendMessage(Utils.fmtClr(String.format("&a&lCOMPASS &8» &7You are now tracking coordinates &f%s&7, &f%s&7, &f%s&7.", x, y, z)));
                    return true;
                }
                catch (Exception e)
                {
                    p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cThe coordinates you entered failed to parse, make sure you are using numbers."));
                }
            }
            else
            {
                p.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cInvalid tracking type."));
                p.sendMessage(Utils.fmtClr("&a&lUSAGE &8» &7/compass trk &fpl &7or &7/compass trk &floc"));
                return true;
            }
        }
        else
        {
            sender.sendMessage(Utils.fmtClr("&a&lCOMPASS &8» &cInvalid arguments."));
            sender.sendMessage(Utils.fmtClr("&a&lUSAGE &8» &7/compass help"));
            return true;
        }

        return false;
    }
}