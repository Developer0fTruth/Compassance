package com.hexragon.compassance.commands;

import com.hexragon.compassance.Main;
import com.hexragon.compassance.configs.ConfigurationPaths;
import com.hexragon.compassance.language.Tags;
import com.hexragon.compassance.managers.themes.Theme;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompassCommand implements CommandExecutor, TabCompleter
{
    private final String[] arg0 = {"help", "open", "theme", "track", "version"};
    private final String[] arg1trk = {"player", "location"};

    public CompassCommand()
    {
        Main.plugin.getCommand("compass").setExecutor(this);
        Main.plugin.getCommand("compass").setTabCompleter(this);
        Main.plugin.getCommand("compass").setPermission("compassance.command");
        Main.plugin.getCommand("compass").setPermissionMessage(Tags.prefix + "&cYou do not have sufficient permission.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("You must be a player to execute this command.");
            return true;
        }

        if (args.length > 0)
        {
            Player p = (Player) sender;

            switch (args[0].toLowerCase())
            {
                case "help":
                    ArrayList<String> arr = new ArrayList<>();
                    arr.add("&8&m--------------------------------------------------");
                    arr.add("");
                    arr.add("        &9&lCompassance &7v&f&n" + Main.plugin.getDescription().getVersion());
                    arr.add("");
                    arr.add("    &f/compass open &8- &7Open compass settings menu.");
                    arr.add("    &f/compass theme -id &8- &7Change theme based on id.");
                    arr.add("    &f/compass track -args... &8- &7Track a target.");
                    arr.add("");
                    arr.add("&8&m--------------------------------------------------");

                    for (String s : arr)
                    {
                        sender.sendMessage(Utils.fmtClr(s));
                    }
                    break;

                case "open":
                    Main.mainMenu.show((Player) sender);
                    break;

                case "theme":


                    if (args.length < 2)
                    {
                        p.sendMessage(Utils.fmtClr(Tags.prefix + "&cInsufficient amount of arguments."));
                        p.sendMessage(Utils.fmtClr(Tags.usage + "&7/compass theme &ftheme-id"));
                        return true;
                    }

                    if (Main.themeManager.getTheme(args[1]) == null)
                    {
                        p.sendMessage(Utils.fmtClr(Tags.prefix + "&cTheme ID doesn't exist."));
                        return true;
                    }
                    else
                    {
                        Theme t = Main.themeManager.getTheme(args[1]);

                        if (Utils.permHandle(p, t.meta.permission, true) || Utils.permHandle(p, "compassance.theme.*", false))
                        {
                            p.sendMessage(Utils.fmtClr(String.format(Tags.prefix + "&7Switching your selected theme to &r%s&7.", Main.themeManager.getTheme(args[1]).meta.name)));
                            Main.playerConfig.config.set(ConfigurationPaths.PlayerConfig.SETTING_SELECTEDTHEME.format(p.getPlayer().getUniqueId().toString()), args[1]);
                            Main.taskManager.refresh(p);
                        }
                        else
                        {
                            p.sendMessage(Utils.fmtClr(Tags.prefix + "&cYou don't have permission for this theme."));
                        }
                    }
                    break;

                case "trk":
                case "track":
                    if (!Main.mainConfig.config.getBoolean(ConfigurationPaths.MainConfig.USE_TRACKING.path))
                    {
                        p.sendMessage(Utils.fmtClr(Tags.prefix + "&cTracking is globally disabled."));
                        return true;
                    }

                    boolean b = Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_TRACKING.format(p.getUniqueId().toString()));
                    if (!b)
                    {
                        p.sendMessage(Utils.fmtClr(Tags.prefix + "&cYou must enable tracking in the Compassance menu."));
                        return true;
                    }

                    if (args.length < 2)
                    {
                        p.sendMessage(Utils.fmtClr(Tags.prefix + "&cInsufficient amount of arguments."));
                        p.sendMessage(Utils.fmtClr(Tags.usage + "&7/compass trk &fpl &7or &7/compass trk &floc"));
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("pl"))
                    {
                        if (args.length != 3)
                        {
                            p.sendMessage(Utils.fmtClr(Tags.prefix + "&cInsufficient amount of arguments."));
                            p.sendMessage(Utils.fmtClr(Tags.usage + "&7/compass trk pl &f-player"));
                            return true;
                        }

                        String targetName = args[2];

                        //noinspection deprecation
                        if (Bukkit.getPlayer(targetName) == p)
                        {
                            p.sendMessage(Utils.fmtClr(Tags.prefix + "&cYou can't track yourself."));
                            return true;
                        }

                        //noinspection deprecation
                        Player pl = Bukkit.getPlayer(targetName);

                        if (pl != null)
                        {
                            boolean b1 = Main.playerConfig.config.getBoolean(ConfigurationPaths.PlayerConfig.SETTING_TRACKING.format(pl.getUniqueId().toString()));
                            if (!b1)
                            {
                                p.sendMessage(Utils.fmtClr(Tags.prefix + "&cThat player must enable tracking to be able to be tracked."));
                                return true;
                            }

                            Main.trackingManager.newTracking(p, pl);
                            Main.taskManager.refresh(p);
                            p.sendMessage(Utils.fmtClr(String.format(Tags.prefix + "&7You are now tracking player &f%s&7.", pl.getName())));
                            pl.sendMessage(Utils.fmtClr(String.format(Tags.prefix + "&7You are being tracked by &f%s&7.", p.getName())));
                            return true;
                        }
                        p.sendMessage(Utils.fmtClr(Tags.prefix + "&cThe player you are attempting to track is not found."));

                        return true;
                    }
                    else if (args[1].equalsIgnoreCase("location") || args[1].equalsIgnoreCase("loc"))
                    {
                        if (args.length != 5)
                        {
                            p.sendMessage(Utils.fmtClr(Tags.prefix + "&cInsufficient amount of arguments."));
                            p.sendMessage(Utils.fmtClr(Tags.usage + "&7/compass trk loc &f-x -y -z&7."));
                            return true;
                        }

                        try
                        {
                            Double x = Double.parseDouble(args[2]);
                            Double y = Double.parseDouble(args[3]);
                            Double z = Double.parseDouble(args[4]);
                            Main.trackingManager.newTracking(p, new Location(p.getWorld(), x, y, z));

                            if (
                                    x > 100000 || x < -100000 ||
                                            y > 1000 || y < 0 ||
                                            z > 100000 || z < -100000
                                    )
                            {
                                p.sendMessage(Utils.fmtClr(Tags.prefix + "&cValues are too large."));
                                return true;
                            }

                            Main.taskManager.refresh(p);
                            p.sendMessage(Utils.fmtClr(String.format(Tags.prefix + "&7You are now tracking coordinates &f%s&7, &f%s&7, &f%s&7.", x, y, z)));
                            return true;
                        }
                        catch (Exception e)
                        {
                            p.sendMessage(Utils.fmtClr(Tags.prefix + "&cThe coordinates you entered failed to parse, make sure you are using numbers."));
                        }
                    }
                    else
                    {
                        p.sendMessage(Utils.fmtClr(Tags.prefix + "&cInvalid tracking type."));
                        p.sendMessage(Utils.fmtClr(Tags.usage + "&7/compass trk &fpl &7or &7/compass trk &floc"));
                        return true;
                    }
                    break;

                case "ver":
                case "version":
                    break;

                default:
                    sender.sendMessage(Utils.fmtClr(Tags.prefix + "&cInvalid arguments."));
                    sender.sendMessage(Utils.fmtClr(Tags.usage + "&7/compass help"));
                    return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        ArrayList<String> list = new ArrayList<>();

        if (!(sender instanceof Player))
        {
            return null;
        }

        Player p = (Player) sender;

        if (args.length == 1)
        {
            if (!args[0].equals(""))
            {
                for (String s : arg0)
                {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) list.add(s);
                }
            }
            else
            {
                Collections.addAll(list, arg0);
            }
        }

        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("track") || args[0].equalsIgnoreCase("trk"))
            {
                if (!args[1].equals(""))
                {
                    for (String s : arg1trk)
                    {
                        if (s.toLowerCase().startsWith(args[1].toLowerCase())) list.add(s);
                    }
                }
                else
                {
                    Collections.addAll(list, arg1trk);
                }
            }

            if (args[0].equalsIgnoreCase("theme"))
            {
                if (!args[1].equals(""))
                {
                    for (Theme t : Main.themeManager.themesAccessibleTo(p))
                    {
                        if (t.id.toLowerCase().startsWith(args[1].toLowerCase())) list.add(t.id);
                    }
                }
                else
                {
                    for (Theme t : Main.themeManager.themesAccessibleTo(p))
                    {
                        list.add(t.id);
                    }
                }
            }
        }

        if (args.length == 3)
        {
            if ((args[0].equalsIgnoreCase("track") || args[0].equalsIgnoreCase("trk")) &&
                    (args[1].equals("player") || (args[1].equals("pl"))))
            {
                if (!args[2].equals(""))
                {
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        if (pl.getName().toLowerCase().startsWith(args[2].toLowerCase())) list.add(pl.getName());
                    }
                }
                else
                {
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        list.add(pl.getName());
                    }
                }
            }
        }

        if ((args[0].equalsIgnoreCase("track") || args[0].equalsIgnoreCase("trk")) &&
                (args[1].equals("location") || (args[1].equals("loc"))))
        {
            if (args.length == 3) list.add(String.valueOf(p.getLocation().getBlockX()));
            if (args.length == 4) list.add(String.valueOf(p.getLocation().getBlockY()));
            if (args.length == 5) list.add(String.valueOf(p.getLocation().getBlockZ()));
        }

        Collections.sort(list);
        return list;
    }
}